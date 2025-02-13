package br.com.onetalk.controller;

import br.com.onetalk.model.Friendship;
import br.com.onetalk.model.Roles;
import br.com.onetalk.service.FriendshipService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import lombok.SneakyThrows;

import javax.security.sasl.AuthenticationException;
import java.util.List;

@Path("/friends")
@RolesAllowed(Roles.USER_ROLE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FriendshipController {

    final FriendshipService service;

    public FriendshipController(FriendshipService service) {
        this.service = service;
    }

    @POST
    @Path("/request")
    @SneakyThrows
    public Response sendFriendRequest(@Context SecurityContext ctx, @QueryParam("friendEmail") String friendEmail) {
        String userEmail  = ctx.getUserPrincipal().getName();
        if (userEmail == null) throw new AuthenticationException("NOT_FOUND_USER_AUTHORIZATION ");
        service.sendFriendRequest(userEmail, friendEmail);
        return Response.ok("Lista de amigos do usuário: " + userEmail ).build();
    }

    @PUT
    @Path("/accept")
    @SneakyThrows
    public Response acceptFriendRequest(@Context SecurityContext ctx, @QueryParam("friendEmail") String friendEmail) {
        String userEmail  = ctx.getUserPrincipal().getName();
        if (userEmail == null) throw new AuthenticationException("NOT_FOUND_USER_AUTHORIZATION ");
        service.acceptFriendRequest(userEmail, friendEmail);
        return Response.ok("Amizade aceita!").build();
    }

    @PUT
    @Path("/reject")
    @SneakyThrows
    public Response rejectFriendRequest(@Context SecurityContext ctx, @QueryParam("friendEmail") String friendEmail) {
        String userEmail  = ctx.getUserPrincipal().getName();
        if (userEmail == null) throw new AuthenticationException("NOT_FOUND_USER_AUTHORIZATION ");
        service.rejectFriendRequest(userEmail, friendEmail);
        return Response.ok("Solicitação de amizade rejeitada!").build();
    }

    @PUT
    @Path("/unfriend")
    @SneakyThrows
    public Response unfriend(@Context SecurityContext ctx, @QueryParam("friendEmail") String friendEmail) {
        String userEmail  = ctx.getUserPrincipal().getName();
        if (userEmail == null) throw new AuthenticationException("NOT_FOUND_USER_AUTHORIZATION ");
        service.unfriend(userEmail, friendEmail);
        return Response.ok("Solicitação de amizade rejeitada!").build();
    }

    @GET
    @Path("/requests")
    @SneakyThrows
    public Response listPendingRequests(@Context SecurityContext ctx) {
        String userEmail  = ctx.getUserPrincipal().getName();
        if (userEmail == null) throw new AuthenticationException("NOT_FOUND_USER_AUTHORIZATION ");
        List<Friendship> pendingRequests = service.getPendingRequests(userEmail);
        return Response.ok(pendingRequests).build();
    }

    @GET
    @SneakyThrows
    public Response listFriends(@Context SecurityContext ctx) {
        String userEmail  = ctx.getUserPrincipal().getName();
        if (userEmail == null) throw new AuthenticationException("NOT_FOUND_USER_AUTHORIZATION ");
        List<Friendship> friends = service.getAcceptedFriends(userEmail);
        return Response.ok(friends).build();
    }

}
