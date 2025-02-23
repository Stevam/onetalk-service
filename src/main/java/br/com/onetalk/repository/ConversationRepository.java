package br.com.onetalk.repository;

import br.com.onetalk.model.Conversation;
import br.com.onetalk.model.Message;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.util.List;

@Slf4j
@ApplicationScoped
public class ConversationRepository implements ReactivePanacheMongoRepository<Conversation> {

    public Uni<Conversation> createConversation(Conversation conversation) {
        return persist(conversation);
    }

    public Uni<List<Conversation>> getUserConversations(ObjectId userId) {
        return find("participants._id", userId).list();
    }

    public Uni<Conversation> getUserConversationsByFriend(ObjectId userId, ObjectId friendId) {
        return find("{ 'participants._id': { $all: [?1, ?2] }, 'participants': { $size: 2 } }", userId, friendId).firstResult();
    }

    public Uni<Conversation> getConversationById(ObjectId conversationId) {
        return findById(conversationId);
    }

    public Uni<Conversation> patchUpdateLastMessage(ObjectId conversationId, Message message) {
        if (conversationId == null || message == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Conversation ID and message must not be null"));
        }
        log.info("Updating last message for conversation: " + conversationId);
        return findById(conversationId)
                .onItem().transformToUni(conversation -> {
                    if (conversation == null) {
                        return Uni.createFrom().failure(new Exception("Conversation not found"));
                    }
                    conversation.setLastMessage(message);
                    return update(conversation)
                            .onItem().transform(conversation1 -> {
                                log.info("Conversation updated successfully");
                                return conversation;
                            })
                            .onFailure().invoke(failure -> {
                                log.error("Erro ao atualizar a conversa: " + failure.getMessage());
                            });
                })
                .onFailure().invoke(failure -> {
                    log.error("Erro ao buscar a conversa: " + failure.getMessage());
                });
    }

}