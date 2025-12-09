package br.com.onetalk.api.controller;

import br.com.onetalk.api.ConversationApi;
import br.com.onetalk.api.request.CreateConversationRequest;
import br.com.onetalk.api.request.Participant;
import br.com.onetalk.api.resource.ConversationResource;
import br.com.onetalk.infrastructure.mappers.Mapper;
import br.com.onetalk.repository.UserRepository;
import br.com.onetalk.service.ConversationService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class ConversationController implements ConversationApi {

    @Inject
    ConversationService conversationService;

    @Inject
    UserRepository userRepository;

    @Inject
    Mapper mapper;

    @Override
    public Uni<ConversationResource> createConversation(CreateConversationRequest request) {
        List<Participant> participants = List.of(request.getParticipant1(), request.getParticipant2());
        return conversationService.createConversation(participants).onItem().transform(mapper::toResource)
                .onFailure().recoverWithItem(throwable -> {
                    if (throwable instanceof WebApplicationException) {
                        throw (WebApplicationException) throwable;
                    } else {
                        throw new WebApplicationException(
                                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(Json.createObjectBuilder().add("message", "An unexpected error occurred").build())
                                        .build()
                        );
                    }
                });
    }

    @Override
    public Uni<List<ConversationResource>> getUserConversations(SecurityContext ctx) {
        String userEmail = ctx.getUserPrincipal().getName();

        return userRepository.findByEmail(userEmail)
                .onItem().transformToUni(user -> {
                    if (user == null) {
                        throw new WebApplicationException(
                                Response.status(Response.Status.NOT_FOUND)
                                        .entity(Json.createObjectBuilder().add("message", "User not found").build())
                                        .build()
                        );
                    }
                    return conversationService.getUserConversations(user.getId()).onItem().transform(mapper::toResourceFromConversations);
                }).onFailure().recoverWithItem(throwable -> {
                    if (throwable instanceof WebApplicationException) {
                        throw (WebApplicationException) throwable;
                    } else {
                        throw new WebApplicationException(
                                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(Json.createObjectBuilder().add("message", "An unexpected error occurred").build())
                                        .build()
                        );
                    }
                });
    }

    @Override
    public Uni<ConversationResource> getUserConversationsByFriend(SecurityContext ctx, ObjectId friendId) {
        String userEmail = ctx.getUserPrincipal().getName();

        return userRepository.findByEmail(userEmail)
                .onItem().transformToUni(user -> {
                    if (user == null) {
                        throw new WebApplicationException(
                                Response.status(Response.Status.NOT_FOUND)
                                        .entity(Json.createObjectBuilder().add("message", "User not found").build())
                                        .build()
                        );
                    }
                    return conversationService.getUserConversationsByFriend(user.getId(), friendId).onItem().transform(mapper::toResource);
                }).onFailure().recoverWithItem(throwable -> {
                    if (throwable instanceof WebApplicationException) {
                        throw (WebApplicationException) throwable;
                    } else {
                        throw new WebApplicationException(
                                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(Json.createObjectBuilder().add("message", "An unexpected error occurred").build())
                                        .build()
                        );
                    }
                });
    }

    @Override
    public Uni<ConversationResource> getConversation(SecurityContext ctx, ObjectId conversationId) {
        return conversationService.getConversationById(conversationId).onItem().transform(mapper::toResource)
                .onFailure().recoverWithItem(throwable -> {
                    if (throwable instanceof WebApplicationException) {
                        throw (WebApplicationException) throwable;
                    } else {
                        throw new WebApplicationException(
                                Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                                        .entity(Json.createObjectBuilder().add("message", "An unexpected error occurred").build())
                                        .build()
                        );
                    }
                });
    }

}
