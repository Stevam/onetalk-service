package br.com.onetalk.api;

import br.com.onetalk.infrastructure.constants.Roles;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("/friendship")
@RolesAllowed(Roles.USER_ROLE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface FriendshipApi {
    @POST
    @Path("/send-invite")
    Uni<Response> sendInvite(@Context SecurityContext ctx, @QueryParam("email") String email);

    @PUT
    @Path("/accept-invite")
    Uni<Response> acceptInvite(@Context SecurityContext ctx, @QueryParam("email") String email);

    @PUT
    @Path("/reject-invite")
    Uni<Response> rejectInvite(@Context SecurityContext ctx, @QueryParam("email") String email);

    @PUT
    @Path("/remove-friend")
    Uni<Response> removeFriend(@Context SecurityContext ctx, @QueryParam("email") String email);

    @GET
    @Path("/invites")
    Uni<Response> listInvites(@Context SecurityContext ctx);

    @GET
    Uni<Response> listFriends(@Context SecurityContext ctx);
}
