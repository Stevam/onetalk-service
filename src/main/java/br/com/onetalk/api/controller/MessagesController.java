package br.com.onetalk.api.controller;

import br.com.onetalk.api.MessagesApi;
import br.com.onetalk.api.resource.MessageResource;
import br.com.onetalk.infrastructure.mappers.Mapper;
import br.com.onetalk.model.Message;
import br.com.onetalk.service.MessageService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class MessagesController implements MessagesApi {

    @Inject
    MessageService messageService;

    @Inject
    Mapper mapper;

    @Override
    public Uni<MessageResource> saveMessage(Message message) {
        return messageService.saveMessage(message).onItem().transform(mapper::toResource);
    }

    @Override
    public Uni<List<MessageResource>> getMessagesByConversation(ObjectId conversationId) {
        return messageService.getMessagesByConversation(conversationId).onItem().transform(mapper::toResourceFromMessages);
    }

}
