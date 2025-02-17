package br.com.onetalk.repository;

import br.com.onetalk.infrastructure.constants.FriendshipStatus;
import br.com.onetalk.model.Friendship;
import br.com.onetalk.model.User;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class FriendshipRepository implements ReactivePanacheMongoRepository<Friendship> {

    public Multi<Friendship> findByUser(String email) {
        return find("userEmail1 = ?1 or userEmail2 = ?1", email).stream();
    }

    public Uni<Friendship> findByEmails(String email1, String email2) {
        String userEmail1 = email1.compareTo(email2) < 0 ? email1 : email2;
        String userEmail2 = email1.compareTo(email2) < 0 ? email2 : email1;

        return find("userEmail1 = ?1 and userEmail2 = ?2", userEmail1, userEmail2).firstResult();
    }

    public Uni<Set<User>> listFriendsByStatus(String userEmail, FriendshipStatus status) {
        return find("(userEmail1 = ?1 or userEmail2 = ?1) and status = ?2", userEmail, status)
                .stream().collect().asList()
                .map(friendshipList -> {
                    Set<User> emails = new HashSet<>();
                    friendshipList.forEach(friendship -> {
                        if (!friendship.getUserEmail1().equals(userEmail)) {
                            //TODO: REVER ESSA LOGICA DOS IDS
                            emails.add(new User(new ObjectId(), friendship.getUserEmail1()));
                        }
                        if (!friendship.getUserEmail2().equals(userEmail)) {
                            //TODO: REVER ESSA LOGICA DOS IDS
                            emails.add(new User(new ObjectId(), friendship.getUserEmail2()));
                        }

                    });
                    return emails;
                });
    }

}