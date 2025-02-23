package br.com.onetalk.api.resource;

import br.com.onetalk.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class MessageResource {

    private ObjectId id;
    private ObjectId conversationId;
    private UserResource sender;
    private String text;
    private LocalDateTime sentAt;
    private boolean seen;

}