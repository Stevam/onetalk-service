package br.com.onetalk.api;

import br.com.onetalk.api.request.CreateConversationRequest;
import br.com.onetalk.api.resource.ConversationResource;
import br.com.onetalk.infrastructure.constants.Roles;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/api/conversation")
@RolesAllowed(Roles.USER_ROLE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface ConversationApi {

    @POST
    public Uni<ConversationResource> createConversation(CreateConversationRequest request);

    @GET
    public Uni<List<ConversationResource>> getUserConversations(@Context SecurityContext ctx);

    @GET
    @Path("/friends/{friendId}/conversation")
    public Uni<ConversationResource> getUserConversationsByFriend(@Context SecurityContext ctx, @PathParam("friendId") ObjectId friendId);

    @GET
    @Path("/{conversationId}")
    public Uni<ConversationResource> getConversation(@Context SecurityContext ctx, @PathParam("conversationId") ObjectId conversationId);

}
