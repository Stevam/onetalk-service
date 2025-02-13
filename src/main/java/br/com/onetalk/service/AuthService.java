package br.com.onetalk.service;

import br.com.onetalk.model.Roles;
import br.com.onetalk.model.User;
import br.com.onetalk.repository.UserRepository;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import lombok.SneakyThrows;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import javax.security.sasl.AuthenticationException;
import java.util.Collections;
import java.util.HashSet;

@ApplicationScoped
public class AuthService {

    final JsonWebToken jwt;
    final UserRepository userRepository;

    public AuthService(JsonWebToken jwt, UserRepository userRepository) {
        this.jwt = jwt;
        this.userRepository = userRepository;
    }


    @SneakyThrows
    public void register(String name, String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new AuthenticationException("Email Already exist");
        }
        User user = new User(name, email, BcryptUtil.bcryptHash(password), new HashSet<>(Collections.singletonList(Roles.USER_ROLE)));
        userRepository.persist(user);
    }

    public String authenticate(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null || !BcryptUtil.matches(password, user.getPasswordHash())) {
            throw new WebApplicationException("Invalid credencials", Response.Status.UNAUTHORIZED);
        }
        return Jwt.issuer("onetalk-api").upn(user.getEmail())
                .claim(Claims.email.name(), user.getEmail())
                .claim(Claims.nickname.name(), user.getEmail())
                .groups(user.getRoles()).sign();
    }

}
