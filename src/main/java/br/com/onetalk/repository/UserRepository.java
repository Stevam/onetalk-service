package br.com.onetalk.repository;

import br.com.onetalk.model.User;
import com.mongodb.client.model.Filters;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;

@ApplicationScoped
public class UserRepository implements ReactivePanacheMongoRepository<User> {

    public Uni<User> findById(String id) {
        return find(Filters.eq("_id", new ObjectId(id))).firstResult();
    }

    public Uni<User> findByEmail(String email) {
        return find(Filters.eq("email", email)).firstResult();
    }

}
