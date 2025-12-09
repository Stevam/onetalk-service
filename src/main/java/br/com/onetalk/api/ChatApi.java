package br.com.onetalk.api;

import br.com.onetalk.model.Message;
import br.com.onetalk.service.ConversationService;
import br.com.onetalk.service.MessageService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.smallrye.jwt.auth.principal.JWTParser;
import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/api/chat/{token}")
public class ChatApi {

    private static final Map<ObjectId, Set<Session>> conversationSessions = new ConcurrentHashMap<>();
    private static final Map<String, Session> userSessions = new ConcurrentHashMap<>();

    @Inject
    private ConversationService conversationService;

    @Inject
    private MessageService messageService;

    @Inject
    private JWTParser jwtParser;

    @OnOpen
    public void onOpen(Session session) {
        String token = session.getPathParameters().get("token");

        if (token != null && !token.isEmpty()) {
            try {
                JsonWebToken jsonWebToken = jwtParser.parse(token);
                String userId = jsonWebToken.getClaim("id").toString();

                session.getUserProperties().put("id", userId);
                session.getUserProperties().put("name", jsonWebToken.getClaim("nickname"));
                session.getUserProperties().put("email", jsonWebToken.getClaim("email"));

                userSessions.put(userId, session);
                log.info("Sessão aberta para o usuário: " + userId);

            } catch (Exception e) {
                log.error("Erro ao processar o token: " + e.getMessage());
                try {
                    session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Invalid token"));
                } catch (IOException ioException) {
                    log.error("Erro ao fechar a sessão: " + ioException.getMessage());
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        String userId = (String) session.getUserProperties().get("id");

        if (userId != null) {
            userSessions.remove(userId);
        }

        getConversationIdFromSession(session).ifPresent(conversationId -> {
            Set<Session> sessions = conversationSessions.get(conversationId);
            if (sessions != null) {
                sessions.remove(session);
            }
        });
    }


    @OnMessage
    @SneakyThrows
    public void onMessage(String messageJson, Session session) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(messageJson);

        // Registro de userId no session
        if (jsonNode.has("from")) {
            String userId = jsonNode.get("from").asText();
            userSessions.put(userId, session);
            session.getUserProperties().put("userId", userId);
        }

        // Mensagens ponto a ponto (vídeo chamada)
        if (jsonNode.has("to") && jsonNode.has("type")) {
            String toUserId = jsonNode.get("to").asText();
            Session targetSession = userSessions.get(toUserId);

            if (targetSession != null && targetSession.isOpen()) {
                targetSession.getAsyncRemote().sendText(messageJson);
                return;
            } else {
                log.warn("Usuário alvo não está conectado: " + toUserId);
                return;
            }
        }

        // Trata mensagens normais (chat em grupo)
        if (jsonNode.has("type") && "joinConversation".equals(jsonNode.get("type").asText())) {
            String conversationId = jsonNode.get("conversationId").asText();
            ObjectId objectId = new ObjectId(conversationId);
            conversationSessions.computeIfAbsent(objectId, k -> ConcurrentHashMap.newKeySet()).add(session);
            session.getUserProperties().put("conversationId", objectId);
            return;
        }

        // Mensagem de texto (salva no banco)
        Message message = parseMessage(messageJson);
        if (message == null || message.getConversationId() == null) {
            log.warn("Mensagem inválida ou conversationId ausente.");
            return;
        }

        ObjectId conversationId = message.getConversationId();
        conversationSessions.computeIfAbsent(conversationId, k -> ConcurrentHashMap.newKeySet()).add(session);
        session.getUserProperties().put("conversationId", conversationId);

        messageService.saveMessage(message)
                .onItem().invoke(savedMessage -> {
                    conversationService.patchUpdateLastMessage(conversationId, savedMessage)
                            .onItem().invoke(updatedConversation -> {
                                broadcastMessage(conversationId, savedMessage);
                            })
                            .subscribe().with(
                                    savedConversation -> {},
                                    failure -> {}
                            );
                }).subscribe().with(
                        savedMessage -> {},
                        failure -> {}
                );
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Erro na sessão: " + throwable.getMessage(), throwable);
    }

    private Optional<ObjectId> getConversationIdFromSession(Session session) {
        Object conversationIdAttr = session.getUserProperties().get("conversationId");
        if (conversationIdAttr instanceof ObjectId) {
            return Optional.of((ObjectId) conversationIdAttr);
        }
        return Optional.empty();
    }

    private void broadcastMessage(ObjectId conversationId, Message message) {
        Set<Session> sessions = conversationSessions.get(conversationId);
        if (sessions != null) {
            String jsonMessage = convertMessageToJson(message);
            for (Session s : sessions) {
                if (s.isOpen()) {
                    s.getAsyncRemote().sendText(jsonMessage, result -> {
                        if (result.getException() != null) {
                            log.error("Erro ao enviar mensagem para a sessão: " + result.getException().getMessage());
                        }
                    });
                }
            }
        }
//        else {
//            messageService.savePendingMessages(conversationId, (PendingMessage) message);
//        }
    }

    private String convertMessageToJson(Message message) {
        return "{" +
                "\"id\": \"" + message.getId().toHexString() + "\", " +
                "\"conversationId\": \"" + message.getConversationId().toHexString() + "\", " +
                "\"text\": \"" + escapeJson(message.getText()) + "\", " +
                "\"sender\": {" +
                "\"id\": \"" + message.getSender().getId().toHexString() + "\", " +
                "\"name\": \"" + escapeJson(message.getSender().getName()) + "\", " +
                "\"email\": \"" + escapeJson(message.getSender().getEmail()) + "\", " +
                "\"profilePicBase64\": \"" + escapeJson(message.getSender().getProfilePicBase64()) + "\"" +
                "}, " +
                "\"sentAt\": \"" + message.getSentAt().toString() + "\", " +
                "\"seen\": " + message.isSeen() +
                "}";
    }

    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private Message parseMessage(String messageJson) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(messageJson, Message.class);
        } catch (IOException e) {
            log.error("Erro ao converter JSON para mensagem: " + e.getMessage());
            return null;
        }
    }

}