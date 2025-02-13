package br.com.onetalk.controller;

import br.com.onetalk.controller.request.UserRequest;
import br.com.onetalk.controller.response.JwtResponse;
import br.com.onetalk.model.Roles;
import br.com.onetalk.service.AuthService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;


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
    public Response login(UserRequest userRequest){
        String token = authService.authenticate(userRequest.getEmail(), userRequest.getPassword());
        NewCookie cookie = new NewCookie.Builder("access_token")
                .value(token)
                .path("/")
                .domain(null)
                .comment("JWT Token")
                .maxAge(3600)
                .secure(true)
                .httpOnly(true)
                .sameSite(NewCookie.SameSite.NONE)
                .build();

        return Response.ok().cookie(cookie).entity(new JwtResponse(token)).build();
    }

    @GET
    @Path("/authenticated")
    @RolesAllowed(Roles.USER_ROLE)
    public Response getUser(@Context HttpHeaders headers, @Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        Cookie cookie = headers.getCookies().get("access_token");
        if (cookie == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok("{\"user\": \"" + username + "\"}").build();
    }

    @POST
    @Path("/logout")
    @RolesAllowed(Roles.USER_ROLE)
    public Response logout() {
        NewCookie expiredCookie = new NewCookie.Builder("access_token")
                .value("")
                .path("/")
                .domain(null)
                .comment("JWT Token")
                .maxAge(0)
                .secure(true)
                .httpOnly(true)
                .build();
        return Response.ok().cookie(expiredCookie).build();
    }

}