package br.com.onetalk.api.controller;

import br.com.onetalk.api.AuthApi;
import br.com.onetalk.api.request.UserRequest;
import br.com.onetalk.service.AuthService;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@ApplicationScoped
public class AuthController implements AuthApi {

    @Inject
    AuthService authService;

    @Override
    public Uni<Response> signUp(UserRequest request) {
        return authService.signUp(request)
                .replaceWith(Response.status(Response.Status.CREATED).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(Response.Status.BAD_REQUEST).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> signIn(UserRequest request) {
        return authService.signIn(request)
                .onItem().transform(user -> Response.ok(user).build())
                .onFailure().recoverWithItem(throwable ->
                        Response.status(((WebApplicationException) throwable).getResponse().getStatus()).entity(throwable.getMessage()).build());
    }

    @Override
    public Uni<Response> signOut() {
        return Uni.createFrom().item(() -> Response.ok("Logged out successfully").build());
        //TODO: implementar black list
        //return authService.signOut();
    }

    @Override
    public Uni<Response> isAuthenticated(SecurityContext securityContext) {
        return Uni.createFrom().item(
                Response.ok().entity("{\"authenticated\": true}").build()
        );
    }
}
