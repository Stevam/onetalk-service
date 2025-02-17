package br.com.onetalk.service;

import br.com.onetalk.api.request.UserRequest;
import br.com.onetalk.infrastructure.constants.Roles;
import br.com.onetalk.infrastructure.exceptions.CustomTokenGenerationException;
import br.com.onetalk.model.User;
import br.com.onetalk.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.infrastructure.Infrastructure;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.CreationException;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.LocalDateTime;
import java.util.Collections;

@ApplicationScoped
public class AuthService {

    @Inject
    UserRepository repository;

    @Inject
    JsonWebToken jwt;

    public Uni<Void> signUp(UserRequest request) {
        if (request.getEmail() == null) {
            return Uni.createFrom().failure(new CreationException("Failure on creating user: email is required."));
        }
        User user = new User(request.getName(), request.getEmail(),
                BcryptUtil.bcryptHash(request.getPassword()), Collections.singleton(Roles.USER_ROLE), LocalDateTime.now());
        return repository.persist(user).replaceWithVoid();
    }

    public Uni<String> signIn(UserRequest request) {
        return repository.findByEmail(request.getEmail())
                .onItem().ifNotNull().transformToUni(user ->
                        verifyPassword(request.getPassword(), user.getPasswordHash())
                                .onItem().transformToUni(isValid -> {
                                    if (!isValid) {
                                        return Uni.createFrom().failure(
                                                //TODO: IMPLEMENTAR DEDICATED EXCEPTION
                                                new WebApplicationException("Invalid credentials.", Response.Status.UNAUTHORIZED)
                                        );
                                    }
                                    return generateTokens(user);
                                })
                )
                .onItem().ifNull().continueWith(() -> {
                    //TODO: IMPLEMENTAR DEDICATED EXCEPTION
                    throw new WebApplicationException("User not found.", Response.Status.NOT_FOUND);
                })
                .onFailure().transform(t ->
                        //TODO: IMPLEMENTAR DEDICATED EXCEPTION
                        new WebApplicationException("Sign-in error: " + t.getMessage(), t, ((WebApplicationException) t).getResponse().getStatus())
                );
    }

    //TODO: IMPLEMENTAR  BLACKLISTTOKEN
    public Uni<Response> signOut() {
        return null;
    }

    private Uni<Boolean> verifyPassword(String rawPassword, String hashedPassword) {
        return Uni.createFrom().item(() -> BcryptUtil.matches(rawPassword, hashedPassword))
                .runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

    private Uni<String> generateTokens(User user) {
        return Uni.createFrom().item(() -> {
            try {
                String accessToken = Jwt.issuer("onetalk-api")
                        .upn(user.getEmail())
                        .claim("id", user.getId())
                        .claim(Claims.email.name(), user.getEmail())
                        .groups(user.getRoles())
                        .expiresIn(900)
                        .sign();

                String refreshToken = Jwt.issuer("onetalk-api")
                        .upn(user.getEmail())
                        .expiresIn(86400)
                        .sign();

                return accessToken + ":" + refreshToken;

            } catch (Exception e) {
                throw new CustomTokenGenerationException("Failed to generate token.", e);
            }
        }).runSubscriptionOn(Infrastructure.getDefaultWorkerPool());
    }

}
