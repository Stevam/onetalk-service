package br.com.onetalk.repository;

import br.com.onetalk.model.Friendship;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FriendshipRepository implements PanacheMongoRepository<Friendship> {

    public Friendship findByFriendEmailAndEmailAndStatus(String friendEmail, String email, Friendship.FriendshipStatus status) {
        return find("friendEmail = ?1 and email = ?2 and status = ?3", friendEmail, email, status).firstResult();
    }

    public Friendship findByEmailAndFriendEmail(String email, String friendEmail) {
        return find("email = ?1 and friendEmail = ?2", email, friendEmail).firstResult();
    }

    public List<Friendship> listAcceptedFriends(String email) {
        return list("(email = ?1 or friendEmail = ?1) and status = ?2", email, Friendship.FriendshipStatus.ACCEPTED);
    }

    public List<Friendship> listPendingRequests(String email) {
        return list("email = ?1 and status = ?2", email, Friendship.FriendshipStatus.PENDING);
    }

}
