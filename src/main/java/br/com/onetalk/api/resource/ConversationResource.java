package br.com.onetalk.api.resource;

import br.com.onetalk.api.request.Participant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Builder
public class ConversationResource {

    private ObjectId id;
    private List<Participant> participants;
    private MessageResource lastMessage;
    private LocalDateTime createdAt;

}