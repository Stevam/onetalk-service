package br.com.onetalk.api;

import br.com.onetalk.api.request.UserRequest;
import br.com.onetalk.api.resource.UserResource;
import br.com.onetalk.infrastructure.constants.Roles;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

@Path("/api/auth")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AuthApi {

    @POST
    @PermitAll
    @Path("/sign-up")
    Uni<Void> signUp(UserRequest request);

    @POST
    @PermitAll
    @Path("/sign-in")
    Uni<UserResource> signIn(UserRequest request);

    @POST
    @PermitAll
    @Path("/sign-out")
    Uni<Void> signOut();

    @GET
    @Path("/authenticated")
    @RolesAllowed(Roles.USER_ROLE)
    Uni<Boolean> isAuthenticated(@Context SecurityContext securityContext);

}
