package br.com.onetalk.api.resource;

import br.com.onetalk.infrastructure.constants.UserStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
public class UserResource {

    private ObjectId id;
    private String name;
    private String email;
    private String token;
    private String profilePicBase64;
    private LocalDateTime createdAt;
    private UserStatus  userStatus;

}
