package br.com.onetalk.infrastructure.mappers;

import br.com.onetalk.api.resource.ConversationResource;
import br.com.onetalk.model.Conversation;

import java.util.List;

public interface ConversationMapper {

    UserMapper userMapper = new UserMapper() {};
    MessageMapper messageMapper = new MessageMapper() {};

    default ConversationResource toResource(Conversation model){
        if (model == null) return null;
        return ConversationResource.builder()
                .id(model.getId())
                .participants(userMapper.toParticipantsFromModels(model.getParticipants()))
                .lastMessage(messageMapper.toResource(model.getLastMessage()))
                .createdAt(model.getCreatedAt())
                .build();
    }

    default Conversation toModel(ConversationResource resource){
        if (resource == null) return null;
        return new Conversation(
                resource.getId(),
                userMapper.toModelsFromParticipants(resource.getParticipants()),
                messageMapper.toModel(resource.getLastMessage()),
                resource.getCreatedAt()
        );
    }

    default List<ConversationResource> toResourceFromConversations(List<Conversation> model){
        if (model == null) return null;
        return model.stream().map(this::toResource).toList();
    }

    default List<Conversation> toModelFromConversationResources(List<ConversationResource> resource){
        if (resource == null) return null;
        return resource.stream().map(this::toModel).toList();
    }
}
