package br.com.onetalk.api;

import br.com.onetalk.api.resource.FriendshipResource;
import br.com.onetalk.infrastructure.constants.Roles;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;

import java.util.Set;

@Path("/api/friendship")
@RolesAllowed(Roles.USER_ROLE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface FriendshipApi {
    @POST
    @Path("/send-invite")
    Uni<Void> sendInvite(@Context SecurityContext ctx, @QueryParam("email") String email);

    @PUT
    @Path("/accept-invite")
    Uni<Void> acceptInvite(@Context SecurityContext ctx, @QueryParam("email") String email);

    @PUT
    @Path("/reject-invite")
    Uni<Void> rejectInvite(@Context SecurityContext ctx, @QueryParam("email") String email);

    @PUT
    @Path("/remove-friend")
    Uni<Void> removeFriend(@Context SecurityContext ctx, @QueryParam("email") String email);

    @GET
    @Path("/invites")
    Uni<Set<FriendshipResource>> listInvites(@Context SecurityContext ctx);

    @GET
    Uni<Set<FriendshipResource>> listFriends(@Context SecurityContext ctx);

    @GET
    @Path("/online")
    Uni<Set<FriendshipResource>> listOfOnlineFriends(@Context SecurityContext ctx);

}
