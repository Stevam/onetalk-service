package br.com.onetalk.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

@MongoEntity(collection = "messages")
public class Message {

    private ObjectId id;
    private ObjectId conversationId;
    private User sender;
    private String text;
    private LocalDateTime sentAt;
    private boolean seen;

    public Message() {
    }

    public Message(ObjectId id, ObjectId conversationId, User sender, String text, LocalDateTime sentAt, boolean seen) {
        this.id = id;
        this.conversationId = conversationId;
        this.sender = sender;
        this.text = text;
        this.sentAt = sentAt;
        this.seen = seen;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getConversationId() {
        return conversationId;
    }

    public void setConversationId(ObjectId conversationId) {
        this.conversationId = conversationId;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }
}