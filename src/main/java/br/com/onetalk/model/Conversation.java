package br.com.onetalk.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

@MongoEntity(collection = "conversations")
public class Conversation {

    private ObjectId id;
    private List<User> participants;
    private Message lastMessage;
    private LocalDateTime createdAt;

    public Conversation() {
    }

    public Conversation(List<User> participants, LocalDateTime createdAt) {
        this.participants = participants;
        this.createdAt = createdAt;
    }

    public Conversation(ObjectId id, List<User> participants, Message lastMessage, LocalDateTime createdAt) {
        this.id = id;
        this.participants = participants;
        this.lastMessage = lastMessage;
        this.createdAt = createdAt;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}