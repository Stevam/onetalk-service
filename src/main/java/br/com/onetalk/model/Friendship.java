package br.com.onetalk.model;

import br.com.onetalk.infrastructure.constants.FriendshipStatus;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@MongoEntity(collection = "friendships")
public class Friendship {

    @Id
    private ObjectId id;
    private String userEmail1;
    private String userEmail2;
    private String userSender;
    private FriendshipStatus status;
    private LocalDateTime createdAt;

    public Friendship() {}

    public Friendship(ObjectId id, String userEmail1, String userEmail2, String userSender, FriendshipStatus status, LocalDateTime createdAt) {
        this.id = id;
        //Sensibility rule: guarantee that userEmail1 is always the smallest for uniqueness, so we can avoid duplicated friendships.
        if (userEmail1.compareTo(userEmail2) < 0) {
            this.userEmail1 = userEmail1;
            this.userEmail2 = userEmail2;
        } else {
            this.userEmail1 = userEmail2;
            this.userEmail2 = userEmail1;
        }
        this.userSender = userSender;
        this.status = status;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUserEmail1() {
        return userEmail1;
    }

    public String setUserEmail1(String userEmail1) {
        return this.userEmail1 = userEmail1;
    }

    public String getUserEmail2() {
        return userEmail2;
    }

    public String setUserEmail2(String userEmail2) {
        return this.userEmail2 = userEmail2;
    }

    public String getUserSender() {
        return userSender;
    }

    public String setUserSender(String userSender) {
        return this.userSender = userSender;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatus(FriendshipStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}