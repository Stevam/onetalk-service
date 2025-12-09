package br.com.onetalk.service;

import br.com.onetalk.api.request.Participant;
import br.com.onetalk.model.Conversation;
import br.com.onetalk.model.Message;
import br.com.onetalk.model.User;
import br.com.onetalk.repository.ConversationRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class ConversationService {

    @Inject
    ConversationRepository conversationRepository;

    public Uni<Conversation> createConversation(List<Participant> participants) {
        List<User> users = participants.stream().map(participant ->
                new User(participant.getId(), participant.getName())).toList();

        Conversation conversation = new Conversation(users, LocalDateTime.now());
        return conversationRepository.createConversation(conversation);
    }

    public Uni<Conversation> patchUpdateLastMessage(ObjectId conversationId, Message message) {
        log.info("Atualizando última mensagem da conversa: " + conversationId);
        return conversationRepository.patchUpdateLastMessage(conversationId, message)
                .onItem().invoke(updatedConversation -> log.info("Conversa atualizada com sucesso: " + updatedConversation))
                .onFailure().invoke(failure -> log.error("Erro ao atualizar a conversa: " + failure.getMessage()));
    }

    public Uni<List<Conversation>> getUserConversations(ObjectId userId) {
        return conversationRepository.getUserConversations(userId);
    }

    public Uni<Conversation> getUserConversationsByFriend(ObjectId userEmail, ObjectId userId) {
        return conversationRepository.getUserConversationsByFriend(userEmail, userId);
    }

    public Uni<Conversation> getConversationById(ObjectId conversationId) {
        return conversationRepository.getConversationById(conversationId);
    }

}
