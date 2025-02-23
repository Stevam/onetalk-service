package br.com.onetalk.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Builder;

@Builder
@MongoEntity(collection = "pending_messages")
public class PendingMessage extends Message{ }