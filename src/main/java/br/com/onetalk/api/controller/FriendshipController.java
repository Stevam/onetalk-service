package br.com.onetalk.api.controller;

import br.com.onetalk.api.FriendshipApi;
import br.com.onetalk.api.resource.FriendshipResource;
import br.com.onetalk.service.FriendshipService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Set;

@ApplicationScoped
public class FriendshipController implements FriendshipApi {

    @Inject
    FriendshipService service;

    @Override
    public Uni<Void> sendInvite(SecurityContext ctx, String email) {
        validateEmail(email);
        String userEmail = ctx.getUserPrincipal().getName();
        return service.sendInvite(userEmail, email)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    @Override
    public Uni<Void> acceptInvite(SecurityContext ctx, String email) {
        validateEmail(email);
        String userEmail = ctx.getUserPrincipal().getName();
        return service.acceptInvite(userEmail, email)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    @Override
    public Uni<Void> rejectInvite(SecurityContext ctx, String email) {
        validateEmail(email);
        String userEmail = ctx.getUserPrincipal().getName();
        return service.rejectInvite(userEmail, email)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    @Override
    public Uni<Void> removeFriend(SecurityContext ctx, String email) {
        validateEmail(email);
        String userEmail = ctx.getUserPrincipal().getName();
        return service.removeFriend(userEmail, email)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    @Override
    public Uni<Set<FriendshipResource>> listInvites(SecurityContext ctx) {
        String userEmail = ctx.getUserPrincipal().getName();
        return service.listInvites(userEmail)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    @Override
    public Uni<Set<FriendshipResource>> listFriends(SecurityContext ctx) {
        String userEmail = ctx.getUserPrincipal().getName();
        return service.listFriends(userEmail)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    @Override
    public Uni<Set<FriendshipResource>> listOfOnlineFriends(SecurityContext ctx) {
        String userEmail = ctx.getUserPrincipal().getName();
        return service.listFriends(userEmail)
                .onFailure().transform(throwable -> new WebApplicationException(
                        Response.status(Response.Status.BAD_REQUEST)
                                .entity(throwable.getMessage())
                                .build()
                ));
    }

    private void validateEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new WebApplicationException(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Friend's email is required.")
                            .build()
            );
        }
    }
}