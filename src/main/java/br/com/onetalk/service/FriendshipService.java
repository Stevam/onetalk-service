package br.com.onetalk.service;

import br.com.onetalk.model.Friendship;
import br.com.onetalk.repository.FriendshipRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class FriendshipService {

    final FriendshipRepository repository;

    public FriendshipService(FriendshipRepository repository) {
        this.repository = repository;
    }

    public void sendFriendRequest(String email, String friendEmail) {
        if (email.equals(friendEmail)) throw new IllegalArgumentException("Você não pode se adicionar como amigo.");

        Friendship existingFriendship = repository.findByEmailAndFriendEmail(email, friendEmail);
        if (existingFriendship != null && existingFriendship.getStatus() != Friendship.FriendshipStatus.REJECTED) {
            throw new IllegalArgumentException("Já existe uma solicitação de amizade ou amizade aceita.");
        }

        Friendship friendship = new Friendship(existingFriendship == null ? null : existingFriendship.getId(),
                email, friendEmail, Friendship.FriendshipStatus.PENDING);

        repository.persistOrUpdate(friendship);
    }

    public void acceptFriendRequest(String email, String friendEmail) {
        Friendship friendship = repository.findByFriendEmailAndEmailAndStatus(friendEmail, email, Friendship.FriendshipStatus.PENDING);

        if (friendship == null) throw new IllegalArgumentException("Solicitação de amizade não encontrada.");

        friendship.setStatus(Friendship.FriendshipStatus.ACCEPTED);
        repository.update(friendship);
    }

    public void rejectFriendRequest(String email, String friendEmail) {
        Friendship friendship = repository.findByFriendEmailAndEmailAndStatus(friendEmail, email, Friendship.FriendshipStatus.PENDING);

        if (friendship == null) throw new IllegalArgumentException("Solicitação de amizade não encontrada.");

        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        repository.update(friendship);
    }

    public void unfriend(String email, String friendEmail) {
        Friendship friendship = repository.findByEmailAndFriendEmail(email, friendEmail);

        if (friendship == null) throw new IllegalArgumentException("Amizade não encontrada.");

        friendship.setStatus(Friendship.FriendshipStatus.REJECTED);
        repository.update(friendship);
    }

    public List<Friendship> getPendingRequests(String email) {
        return repository.listPendingRequests(email);
    }

    public List<Friendship> getAcceptedFriends(String email) {
        return repository.listAcceptedFriends(email);
    }

}
