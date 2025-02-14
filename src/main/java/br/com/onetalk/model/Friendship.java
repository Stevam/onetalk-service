package br.com.onetalk.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MongoEntity(collection = "friendship")
public class Friendship {

    @Id
    private ObjectId id;
    private String email;
    private String friendEmail;
    private FriendshipStatus status;

    public enum FriendshipStatus {
        PENDING,
        ACCEPTED,
        REJECTED
    }

}