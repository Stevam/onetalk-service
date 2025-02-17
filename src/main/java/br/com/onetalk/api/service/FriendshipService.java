package br.com.onetalk.api.service;

import br.com.onetalk.infrastructure.constants.FriendshipStatus;
import br.com.onetalk.model.Friendship;
import br.com.onetalk.model.User;
import br.com.onetalk.repository.FriendshipRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.Set;

@ApplicationScoped
public class FriendshipService {

    @Inject
    FriendshipRepository repository;

    public Uni<Void> sendInvite(String userEmail, String email) {
        if (userEmail.equals(email)) throw new IllegalArgumentException("You can't add yourself as a friend.");
        //TODO: VALIDAR SE O AMIGO È UM EMAIL VALIDO

        return repository.findByEmails(userEmail, email)
                .onItem().ifNotNull().transformToUni(existingFriendship -> {
                    // TODO: VALIDAR USUARIO BLOCKED
                    if (existingFriendship.getStatus() != FriendshipStatus.REJECTED) {
                        return Uni.createFrom().failure(new IllegalArgumentException("There is already a friendship request or accepted friendship."));
                    }
                    existingFriendship.setStatus(FriendshipStatus.PENDING);
                    return repository.persistOrUpdate(existingFriendship);
                })
                .onItem().ifNull().switchTo(() -> {
                    Friendship friendship = new Friendship(null, userEmail, email, FriendshipStatus.PENDING, LocalDateTime.now());
                    return repository.persistOrUpdate(friendship);
                }).replaceWithVoid();
    }

    public  Uni<Void> acceptInvite(String userEmail, String email) {
        if (userEmail.equals(email)) throw new IllegalArgumentException("You can't accept your own friendship request.");

        return repository.findByEmails(userEmail, email)
                .onItem().ifNotNull().transformToUni(existingFriendship -> {
                    if (existingFriendship.getStatus() != FriendshipStatus.PENDING) {
                        return Uni.createFrom().failure(new IllegalArgumentException("Friendship is not pending."));
                    }
                    existingFriendship.setStatus(FriendshipStatus.ACCEPTED);
                    return repository.update(existingFriendship);
                })
                .onItem().ifNull().switchTo(() -> Uni.createFrom().failure(new IllegalArgumentException("Friendship request not found.")))
                .replaceWithVoid();
    }

    public Uni<Void> rejectInvite(String userEmail, String email) {
        if (userEmail.equals(email)) throw new IllegalArgumentException("You can't reject your own friendship request.");

        return repository.findByEmails(userEmail, email)
                .onItem().ifNotNull().transformToUni(existingFriendship -> {
                    if (existingFriendship.getStatus() != FriendshipStatus.PENDING) {
                        return Uni.createFrom().failure(new IllegalArgumentException("Friendship is not pending."));
                    }
                    existingFriendship.setStatus(FriendshipStatus.REJECTED);
                    return repository.update(existingFriendship);
                })
                .onItem().ifNull().switchTo(() -> Uni.createFrom().failure(new IllegalArgumentException("Friendship request not found.")))
                .replaceWithVoid();
    }

    public Uni<Void> removeFriend(String userEmail, String email) {
        if (userEmail.equals(email)) throw new IllegalArgumentException("You can't remove yourself as a friend.");

        return repository.findByEmails(userEmail, email)
                .onItem().ifNotNull().transformToUni(existingFriendship -> {
                    existingFriendship.setStatus(FriendshipStatus.BROKEN);
                    return repository.update(existingFriendship);
                })
                .onItem().ifNull().switchTo(() -> Uni.createFrom().failure(new IllegalArgumentException("Friendship request not found.")))
                .replaceWithVoid();
    }

    public Uni<Set<User>> listInvites(String userEmail) {
        //TODO: RETONAR OS USUARIOS
        return repository.listFriendsByStatus(userEmail, FriendshipStatus.PENDING);
    }

    public Uni<Set<User>> listFriends(String userEmail) {
        //TODO: RETONAR OS USUARIOS
        return repository.listFriendsByStatus(userEmail, FriendshipStatus.ACCEPTED);
    }
}
