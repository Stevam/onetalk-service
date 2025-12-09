package br.com.onetalk.service;

import br.com.onetalk.model.Message;
import br.com.onetalk.model.PendingMessage;
import br.com.onetalk.repository.MessageRepository;
import br.com.onetalk.repository.PendingMessageRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;

@Slf4j
@ApplicationScoped
public class MessageService {

    @Inject
    MessageRepository messageRepository;

    @Inject
    PendingMessageRepository pendingMessageRepository;

    public Uni<Message> saveMessage(Message message) {
        log.info("Salvando mensagem: " + message);
        return messageRepository.saveMessage(message)
                .onItem().invoke(savedMessage -> log.info("Mensagem salva com sucesso: " + savedMessage))
                .onFailure().invoke(failure -> log.error("Erro ao salvar a mensagem: " + failure.getMessage()));
    }

    public Uni<List<Message>> getMessagesByConversation(ObjectId conversationId) {
        return messageRepository.getMessagesByConversation(conversationId);
    }


    public Uni<PendingMessage> savePendingMessages(ObjectId conversationId, PendingMessage PendingMessage) {
        log.info("Salvando mensagem: " + PendingMessage);
        return pendingMessageRepository.savePendingMessage(PendingMessage)
                .onItem().invoke(savedMessage -> log.info("Mensagem salva com sucesso: " + savedMessage))
                .onFailure().invoke(failure -> log.error("Erro ao salvar a mensagem: " + failure.getMessage()));
    }

    public Uni<PendingMessage> deleteAllPendingMessagesByConversationId(PendingMessage PendingMessage) {
        log.info("Salvando mensagem: " + PendingMessage);
        return pendingMessageRepository.savePendingMessage(PendingMessage)
                .onItem().invoke(savedMessage -> log.info("Mensagem salva com sucesso: " + savedMessage))
                .onFailure().invoke(failure -> log.error("Erro ao salvar a mensagem: " + failure.getMessage()));
    }

    public Uni<List<PendingMessage>> getPendingMessagesByConversation(ObjectId conversationId) {
        return  pendingMessageRepository.getPendingMessagesByConversationId(conversationId);
    }
}