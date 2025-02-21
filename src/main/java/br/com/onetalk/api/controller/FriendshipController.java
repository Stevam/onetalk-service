package br.com.onetalk.api.controller;

import br.com.onetalk.api.FriendshipApi;
import br.com.onetalk.api.service.FriendshipService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
public class FriendshipController implements FriendshipApi {

    @Inject
    FriendshipService service;

    @Override
    public Uni<Response> sendInvite(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder()
                .add("message", "Friend's email is required.").build().toString()).build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.sendInvite(userEmail, email)
                .replaceWith(Response.ok(Json.createObjectBuilder()
                        .add("message", "Friend request sent to: " + email).build().toString()).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> acceptInvite(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder()
                .add("message", "Friend's email is required.").build().toString()).build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.acceptInvite(userEmail, email)
                .replaceWith(Response.ok(Json.createObjectBuilder()
                        .add("message", "Friendship accepted!").build().toString()).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> rejectInvite(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder()
                .add("message", "Friend's email is required.").build().toString()).build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.rejectInvite(userEmail, email)
                .replaceWith(Response.ok(Json.createObjectBuilder()
                        .add("message", "Friendship rejected!").build().toString()).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> removeFriend(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity(Json.createObjectBuilder()
                .add("message", "Friend's email is required.").build().toString()).build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.removeFriend(userEmail, email)
                .replaceWith(Response.ok(Json.createObjectBuilder()
                        .add("message", "Friendship broken!").build().toString()).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> listInvites(SecurityContext ctx) {
        String userEmail  = ctx.getUserPrincipal().getName();

        return service.listInvites(userEmail)
                .onItem().transform(friendships -> {
                    if (friendships.isEmpty()) {
                        return Response.noContent().entity(Json.createObjectBuilder()
                                .add("message", "No friendship requests found.").build().toString()).build();
                    }
                    return Response.ok(friendships).build();
                })
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> listFriends(SecurityContext ctx) {
        String userEmail  = ctx.getUserPrincipal().getName();

        return service.listFriends(userEmail)
                .onItem().transform(friendships -> {
                    if (friendships.isEmpty()) {
                        return Response.noContent().entity(Json.createObjectBuilder()
                                .add("message", "No friendship found.").build().toString()).build();
                    }
                    return Response.ok(friendships).build();
                })
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }
}
