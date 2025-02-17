package br.com.onetalk.api;

import br.com.onetalk.api.request.UserRequest;
import br.com.onetalk.infrastructure.constants.Roles;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/auth")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthApi {

    @POST
    @PermitAll
    @Path("/sign-up")
    Uni<Response> signUp(UserRequest request);

    @POST
    @PermitAll
    @Path("/sign-in")
    Uni<Response> signIn(UserRequest request);

    @POST
    @PermitAll
    @Path("/sign-out")
    Uni<Response> signOut();

    @GET
    @Path("/authenticated")
    @RolesAllowed(Roles.USER_ROLE)
    Uni<Response> isAuthenticated(@Context SecurityContext securityContext);

}
