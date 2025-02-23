package br.com.onetalk.service;

import br.com.onetalk.api.resource.FriendshipResource;
import br.com.onetalk.api.resource.UserResource;
import br.com.onetalk.infrastructure.constants.FriendshipStatus;
import br.com.onetalk.model.Friendship;
import br.com.onetalk.repository.FriendshipRepository;
import br.com.onetalk.repository.UserRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped
public class FriendshipService {

    @Inject
    FriendshipRepository repository;

    @Inject
    UserRepository userRepository;

    public Uni<Void> sendInvite(String userEmail, String email) {
        if (userEmail.equals(email)) throw new IllegalArgumentException("You can't add yourself as a friend.");
        //TODO: VALIDAR SE O AMIGO È UM EMAIL EXISTENTE

        return repository.findByEmails(userEmail, email)
                .onItem().ifNotNull().transformToUni(existingFriendship -> {
                    // TODO: VALIDAR USUARIO BLOCKED
                    if (existingFriendship.getStatus() == FriendshipStatus.BLOCKED) {
                        return Uni.createFrom().failure(new IllegalArgumentException("User not found."));
                    }

                    Set<FriendshipStatus> invalidStatuses = EnumSet.of(
                            FriendshipStatus.PENDING,
                            FriendshipStatus.ACCEPTED
                    );

                    if (invalidStatuses.contains(existingFriendship.getStatus())) {
                        return Uni.createFrom().failure(new IllegalArgumentException("A friendship request already exists or has been accepted."));
                    }

                    existingFriendship.setUserSender(userEmail);
                    existingFriendship.setStatus(FriendshipStatus.PENDING);
                    return repository.persistOrUpdate(existingFriendship);
                })
                .onItem().ifNull().switchTo(() -> {
                    Friendship friendship = new Friendship(null, userEmail, email, userEmail, FriendshipStatus.PENDING, LocalDateTime.now());
                    return repository.persistOrUpdate(friendship);
                }).replaceWithVoid();
    }

    public Uni<Void> acceptInvite(String userEmail, String email) {
        if (userEmail.equals(email))
            throw new IllegalArgumentException("You can't accept your own friendship request.");

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
        if (userEmail.equals(email))
            throw new IllegalArgumentException("You can't reject your own friendship request.");

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

    public Uni<Set<FriendshipResource>> listInvites(String userEmail) {
        return repository.listFriendsByStatus(userEmail, FriendshipStatus.PENDING).onItem()
                .transform(friendships -> {
                    Set<FriendshipResource> responses = new HashSet<>();

                    if (friendships.isEmpty()) {
                        return responses;
                    }

                    friendships.forEach(friendship -> {
                        //TODO: RETONAR OS USUARIOS
                        String email = !Objects.equals(friendship.getUserEmail1(), userEmail) ? friendship.getUserEmail1() : friendship.getUserEmail2();

                        //TODO: criar mapper
                        FriendshipResource response = FriendshipResource.builder().id(friendship.getId()).friendEmail(email)
                                .userSender(friendship.getUserSender()).friend(null).status(friendship.getStatus())
                                .createdAt(friendship.getCreatedAt()).build();
                        responses.add(response);
                    });

                    return responses;
                });
    }

    public Uni<Set<FriendshipResource>> listFriends(String userEmail) {
        return repository.listFriendsByStatus(userEmail, FriendshipStatus.ACCEPTED)
                .onItem().transformToMulti(friendships -> Multi.createFrom().iterable(friendships))
                .onItem().transformToUniAndMerge(friendship -> {
                    String friendEmail = !Objects.equals(friendship.getUserEmail1(), userEmail)
                            ? friendship.getUserEmail1()
                            : friendship.getUserEmail2();

                    return userRepository.findByEmail(friendEmail)
                            .onItem().transform(user -> {

                                //TODO: criar mapper
                                UserResource friend = UserResource.builder().id(user.getId()).name(user.getName())
                                        .email(user.getEmail()).profilePicBase64(user.getProfilePicBase64())
                                        .createdAt(user.getCreatedAt()).userStatus(user.getUserStatus()).build();

                                //TODO: criar mapper
                                return FriendshipResource.builder().id(friendship.getId()).friendEmail(friendEmail)
                                        .userSender(friendship.getUserSender()).friend(friend).status(friendship.getStatus())
                                        .createdAt(friendship.getCreatedAt()).build();
                            });
                })
                .collect().asSet();
    }
}
