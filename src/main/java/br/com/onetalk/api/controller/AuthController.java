package br.com.onetalk.api.controller;

import br.com.onetalk.api.AuthApi;
import br.com.onetalk.api.request.UserRequest;
import br.com.onetalk.api.resource.UserResource;
import br.com.onetalk.infrastructure.mappers.Mapper;
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
    @Inject
    Mapper mapper;

    @Override
    public Uni<Void> signUp(UserRequest request) {
        return authService.getUser(request.getEmail())
                .onItem().transformToUni(existingUser -> {
                    if (existingUser != null) {
                        throw new WebApplicationException(
                                Response.status(Response.Status.CONFLICT)
                                        .entity("User with this email already exists")
                                        .build()
                        );
                    }
                    return authService.signUp(request)
                            .onFailure().transform(this::handleFailure);
                });
    }

    @Override
    public Uni<UserResource> signIn(UserRequest request) {
        return authService.signIn(request).onItem().transform(mapper::toResource)
                .onFailure().transform(this::handleFailure);
    }

    @Override
    public Uni<Void> signOut() {
        // TODO: Implementar blacklist de tokens
        return Uni.createFrom().voidItem();
    }

    @Override
    public Uni<Boolean> isAuthenticated(SecurityContext securityContext) {
        return Uni.createFrom().item(true);
    }

    private WebApplicationException handleFailure(Throwable throwable) {
        if (throwable instanceof WebApplicationException) {
            return (WebApplicationException) throwable;
        }
        return new WebApplicationException(
                Response.status(Response.Status.BAD_REQUEST)
                        .entity(throwable.getMessage())
                        .build()
        );
    }
}