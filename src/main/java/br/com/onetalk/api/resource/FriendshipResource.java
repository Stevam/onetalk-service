package br.com.onetalk.api.resource;

import br.com.onetalk.infrastructure.constants.FriendshipStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class FriendshipResource {

    private ObjectId id;
    private String friendEmail;
    private String userSender;
    private UserResource friend;
    private FriendshipStatus status;
    private LocalDateTime createdAt;

}