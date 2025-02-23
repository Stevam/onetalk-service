package br.com.onetalk.repository;

import br.com.onetalk.model.PendingMessage;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ApplicationScoped
public class PendingMessageRepository implements ReactivePanacheMongoRepository<PendingMessage> {

    public Uni<PendingMessage> savePendingMessage(PendingMessage message) {
        message.setSentAt(LocalDateTime.now());

        return Uni.createFrom().item(message)
                .onItem().transformToUni(msg -> persist(msg))
                .onItem().invoke(persistedMessage -> {
                    log.info("Mensagem persistida com sucesso: " + persistedMessage);
                })
                .onFailure().invoke(failure -> {
                    log.error("Erro ao salvar a mensagem: " + failure.getMessage());
                });
    }

    public Uni<List<PendingMessage>> getPendingMessagesByConversationId(ObjectId conversationId) {
        return find("conversationId", conversationId).list();
    }
}