package br.com.onetalk.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@MongoEntity(collection = "users")
public class User {

    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String passwordHash;
    private Set<String> roles;

    public User(String name, String email, String passwordHash, Set<String> roles) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
    }

}