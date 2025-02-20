package br.com.onetalk.api.response;

import br.com.onetalk.infrastructure.constants.FriendshipStatus;
import br.com.onetalk.model.User;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class FriendshipResponse {

    private ObjectId id;
    private String friendEmail;
    private String userSender;
    private User friend;
    private FriendshipStatus status;
    private LocalDateTime createdAt;

    public FriendshipResponse() {}

    public FriendshipResponse(ObjectId id, String friendEmail, String userSender, User friend, FriendshipStatus status, LocalDateTime createdAt) {
        this.id = id;
        this.friendEmail = friendEmail;
        this.userSender = userSender;
        this.friend = friend;
        this.status = status;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getFriendEmail() {
        return friendEmail;
    }

    public void setFriendEmail(String friendEmail) {
        this.friendEmail = friendEmail;
    }

    public String getUserSender() {
        return userSender;
    }

    public void setUserSender(String userSender) {
        this.userSender = userSender;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
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