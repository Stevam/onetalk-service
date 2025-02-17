package br.com.onetalk.api.controller;

import br.com.onetalk.api.FriendshipApi;
import br.com.onetalk.api.service.FriendshipService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
public class FriendshipController implements FriendshipApi {

    @Inject
    FriendshipService service;

    @Override
    public Uni<Response> sendInvite(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Friend's email is required.").build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.sendInvite(userEmail, email)
                .replaceWith(Response.ok("Friend request sent to: " + email).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> acceptInvite(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Friend's email is required.").build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.acceptInvite(userEmail, email)
                .replaceWith(Response.ok("Friendship accepted!").build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> rejectInvite(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Friend's email is required.").build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.rejectInvite(userEmail, email)
                .replaceWith(Response.ok("Friendship rejected!").build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> removeFriend(SecurityContext ctx, String email) {
        if (email == null) return Uni.createFrom().item(Response.status(Response.Status.BAD_REQUEST).entity("Friend's email is required.").build());
        String userEmail  = ctx.getUserPrincipal().getName();
        return service.removeFriend(userEmail, email)
                .replaceWith(Response.ok("Friendship broken!").build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> listInvites(SecurityContext ctx) {
        String userEmail  = ctx.getUserPrincipal().getName();

        return service.listInvites(userEmail)
                .onItem().transform(friendships -> {
                    if (friendships.isEmpty()) {
                        return Response.noContent().entity("No friendship requests found.").build();
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
                        return Response.noContent().entity("No friendship found.").build();
                    }
                    return Response.ok(friendships).build();
                })
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }
}
