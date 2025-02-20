package br.com.onetalk.model;

import br.com.onetalk.infrastructure.constants.UserStatus;
import io.quarkus.mongodb.panache.common.MongoEntity;
import jakarta.persistence.Id;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@MongoEntity(collection = "users")
public class User {

    @Id
    private ObjectId id;
    private String name;
    private String email;
    private String passwordHash;
    private String profilePic;
    private String profilePicBase64;
    private String token;
    private Set<String> roles;
    private LocalDateTime createdAt;
    private UserStatus  userStatus;
    private List<ObjectId> conversationIds;

    public User() {
    }

    public User(String name, String email, String passwordHash, String profilePic, String profilePicBase64, String token,
                Set<String> roles, LocalDateTime createdAt, UserStatus userStatus, List<ObjectId> conversationIds) {

        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePic = profilePic;
        this.profilePicBase64 = profilePicBase64;
        this.token = token;
        this.roles = roles;
        this.createdAt = createdAt;
        this.userStatus = userStatus;
        this.conversationIds = conversationIds;
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
                ", token='" + token + '\'' +
                ", roles=" + roles +
                ", createdAt=" + createdAt +
                ", userStatus=" + userStatus +
                ", conversationIds=" + conversationIds +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(name, user.name) && Objects.equals(email, user.email) && Objects.equals(passwordHash, user.passwordHash) && Objects.equals(profilePic, user.profilePic) && Objects.equals(profilePicBase64, user.profilePicBase64) && Objects.equals(token, user.token) && Objects.equals(roles, user.roles) && Objects.equals(createdAt, user.createdAt) && userStatus == user.userStatus && Objects.equals(conversationIds, user.conversationIds);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, passwordHash, profilePic, profilePicBase64, token, roles, createdAt, userStatus, conversationIds);
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public List<ObjectId> getConversationIds() {
        return conversationIds;
    }

    public void setConversationIds(List<ObjectId> conversationIds) {
        this.conversationIds = conversationIds;
    }
}
