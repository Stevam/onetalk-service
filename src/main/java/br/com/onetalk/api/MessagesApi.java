package br.com.onetalk.api;

import br.com.onetalk.api.resource.MessageResource;
import br.com.onetalk.infrastructure.constants.Roles;
import br.com.onetalk.model.Message;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.bson.types.ObjectId;

import java.util.List;

@Path("/api/message")
@RolesAllowed(Roles.USER_ROLE)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface MessagesApi {

    @POST
    public Uni<MessageResource> saveMessage(Message message);

    @GET
    @Path("/{conversationId}")
    public Uni<List<MessageResource>> getMessagesByConversation(@PathParam("conversationId") ObjectId conversationId);

}
