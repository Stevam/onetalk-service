package br.com.onetalk.infrastructure.mappers;

import br.com.onetalk.api.request.Participant;
import br.com.onetalk.api.resource.UserResource;
import br.com.onetalk.model.User;

import java.util.List;

public interface UserMapper {

    default UserResource toResource(User model) {
        if (model == null) return null;
        return UserResource.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .token(model.getToken())
                .profilePicBase64(model.getProfilePicBase64())
                .createdAt(model.getCreatedAt())
                .userStatus(model.getUserStatus())
                .build();
    }

    default User toModel(UserResource resource) {
        if (resource == null) return null;
        return new User(
                resource.getId(),
                resource.getName(),
                resource.getEmail(),
//                .token(model.getToken())
                resource.getProfilePicBase64(),
                resource.getCreatedAt(),
                resource.getUserStatus()
        );
    }

    default Participant toParticipant(User model) {
        if (model == null) return null;
        return Participant.builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .build();
    }

    default User toModel(Participant participant) {
        if (participant == null) return null;
        return new User(
                participant.getId(),
                participant.getName(),
                participant.getEmail(),
                null,
                null,
                null
        );
    }

    default Participant toParticipant(UserResource resource) {
        if (resource == null) return null;
        return Participant.builder()
                .id(resource.getId())
                .name(resource.getName())
                .email(resource.getEmail())
                .build();
    }

    default UserResource toResource(Participant participant) {
        if (participant == null) return null;
        return UserResource.builder()
                .id(participant.getId())
                .name(participant.getName())
                .email(participant.getEmail())
                .build();
    }

    default List<Participant> toParticipantsFromModels(List<User> models){
        if (models == null) return null;
        return models.stream().map(this::toParticipant).toList();
    }

    default List<User> toModelsFromParticipants(List<Participant> participants){
        if (participants == null) return null;
        return participants.stream().map(this::toModel).toList();
    }

    default List<Participant> toParticipantsFromResources(List<UserResource> resources){
        if (resources == null) return null;
        return resources.stream().map(this::toParticipant).toList();
    }

    default List<UserResource> toResourcesFromParticipants(List<Participant> participants){
        if (participants == null) return null;
        return participants.stream().map(this::toResource).toList();
    }


    default List<UserResource> toResourcesFromUsers(List<User> model) {
        if (model == null) return null;
        return model.stream().map(this::toResource).toList();
    }

    default List<User> toModelsFromUserResources(List<UserResource> resources) {
        if (resources == null) return null;
        return resources.stream().map(this::toModel).toList();
    }

}
