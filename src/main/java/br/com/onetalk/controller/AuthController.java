package br.com.onetalk.controller;

import br.com.onetalk.controller.request.UserRequest;
import br.com.onetalk.model.Roles;
import br.com.onetalk.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.Map;


@Path("/auth")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthController {

    final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @POST
    @PermitAll
    @Path("/register")
    public Response register(UserRequest request) {
        authService.register(request.getName(), request.getEmail(), request.getPassword());
        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @PermitAll
    @Path("/login")
    public Response login(UserRequest userRequest) {
        String token = authService.authenticate(userRequest.getEmail(), userRequest.getPassword());
        return Response.ok(Map.of("token", token)).build();
    }

    @GET
    @Path("/authenticated")
    @RolesAllowed(Roles.USER_ROLE)
    public Response isAuthenticated(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        return Response.ok(Map.of("user", username)).build();
    }

    @POST
    @Path("/logout")
    public Response logout() {
        return Response.ok("Logout realizado com sucesso").build();
    }

}