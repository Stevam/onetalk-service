package br.com.onetalk.infrastructure.mappers;

import br.com.onetalk.api.resource.MessageResource;
import br.com.onetalk.model.Message;

import java.util.List;

public interface MessageMapper {

    UserMapper userMapper = new UserMapper() {};

    default MessageResource toResource(Message model){
        if (model == null) return null;
        return MessageResource.builder()
                .id(model.getId())
                .conversationId(model.getId())
                .sender(userMapper.toResource(model.getSender()))
                .text(model.getText())
                .sentAt(model.getSentAt())
                .seen(model.isSeen())
                .build();
    }

    default Message toModel(MessageResource resource){
        if (resource == null) return null;
        return new Message(
                resource.getId(),
                resource.getConversationId(),
                userMapper.toModel(resource.getSender()),
                resource.getText(),
                resource.getSentAt(),
                resource.isSeen()
        );
    }

    default List<MessageResource> toResourceFromMessages(List<Message> model){
        if (model == null) return null;
        return model.stream().map(this::toResource).toList();
    }
    default List<Message> toModelFromMessagesResources(List<MessageResource> resource){
        if (resource == null) return null;
        return resource.stream().map(this::toModel).toList();
    }
}
