package br.com.onetalk.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@MongoEntity(collection = "users")
public class User {

    private ObjectId id;
    private String name;
    private String email;
    private String passwordHash;
    private String profilePic;
    private String profilePicBase64;
    private Set<String> roles;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String name, String email, String passwordHash, Set<String> roles, LocalDateTime createdAt) {
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles;
        this.createdAt = createdAt;
    }

    public User(ObjectId id, String email) {
        this.id = id;
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", profilePic='" + profilePic + '\'' +
                ", profilePicBase64='" + profilePicBase64 + '\'' +
                ", roles=" + roles +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name)
                && Objects.equals(email, user.email) && Objects.equals(passwordHash, user.passwordHash)
                && Objects.equals(profilePic, user.profilePic) && Objects.equals(profilePicBase64, user.profilePicBase64)
                && Objects.equals(roles, user.roles) && Objects.equals(createdAt, user.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, passwordHash, profilePic, profilePicBase64, roles, createdAt);
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePicBase64() {
        return profilePicBase64;
    }

    public void setProfilePicBase64(String profilePicBase64) {
        this.profilePicBase64 = profilePicBase64;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
