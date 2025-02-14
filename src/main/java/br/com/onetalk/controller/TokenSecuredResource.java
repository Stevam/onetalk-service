package br.com.onetalk.controller;

import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.InternalServerErrorException;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.core.UriInfo;
import org.eclipse.microprofile.jwt.Claim;
import org.eclipse.microprofile.jwt.Claims;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Arrays;
import java.util.HashSet;

@Path("/secured")
@RequestScoped
public class TokenSecuredResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    @Claim(standard = Claims.birthdate)
    String birthdate;

    @GET
    @Path("login")
    @PermitAll
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@Context SecurityContext ctx, @Context UriInfo uriInfo) {
        return generateToken();
    }

    @GET
    @Path("permit-all")
    @PermitAll 
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@Context SecurityContext ctx, @Context UriInfo uriInfo) {
        return getResponseString(ctx); 
    }

    @GET
    @Path("roles-allowed")
    @RolesAllowed({ "User", "Admin" })
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowed(@Context SecurityContext ctx, @Context UriInfo uriInfo) {
        return getResponseString(ctx) + ", birthdate: " + jwt.getClaim("birthdate").toString();
    }

    @GET
    @Path("roles-allowed-admin")
    @RolesAllowed("Admin")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloRolesAllowedAdmin(@Context SecurityContext ctx) {
        return getResponseString(ctx) + ", birthdate: " + birthdate;
    }

    private String generateToken(){
        String token =
                Jwt.issuer("onetalk-api")
                        .upn("jdoe@quarkus.io")
                        .groups(new HashSet<>(Arrays.asList("User", "Admin")))
                        .claim(Claims.birthdate.name(), "2001-07-13")
                        .sign();

        return token;
    }

    private String getResponseString(SecurityContext ctx) {
        String name;
        if (ctx.getUserPrincipal() == null) { 
            name = "anonymous";
        } else if (!ctx.getUserPrincipal().getName().equals(jwt.getName())) { 
            throw new InternalServerErrorException("Principal and JsonWebToken names do not match");
        } else {
            name = ctx.getUserPrincipal().getName(); 
        }
        return String.format("hello %s,"
            + " isHttps: %s,"
            + " authScheme: %s,"
            + " hasJWT: %s",
            name, ctx.isSecure(), ctx.getAuthenticationScheme(), hasJwt()); 
    }

    private boolean hasJwt() {
        return jwt.getClaimNames() != null;
    }
}