package br.com.onetalk.repository;

import br.com.onetalk.model.User;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {
    public User findByEmail(String email){
        return find("email",email).firstResult();
    }
}
