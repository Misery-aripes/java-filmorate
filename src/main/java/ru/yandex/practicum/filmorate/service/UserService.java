package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUser(int id) {
        return userStorage.getUser(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + id + " not found"));
    }

    public User deleteUser(int id) {
        return userStorage.deleteUser(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + id + " not found"));
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public List<User> addFriends(int idOfPerson1, int idOfPerson2) {
        User user1 = userStorage.getUser(idOfPerson1)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + idOfPerson1 + " not found"));
        User user2 = userStorage.getUser(idOfPerson2)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + idOfPerson2 + " not found"));

        if (user1.getFriends().contains(idOfPerson2)) {
            throw new InternalServerException("Users are already friends");
        }

        user1.addFriend(idOfPerson2);
        user2.addFriend(idOfPerson1);

        log.info("Users with id: {} and {} are now friends", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }

    public List<User> deleteFriends(int idOfPerson1, int idOfPerson2) {
        User user1 = userStorage.getUser(idOfPerson1)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + idOfPerson1 + " not found"));
        User user2 = userStorage.getUser(idOfPerson2)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + idOfPerson2 + " not found"));

        user1.removeFriend(idOfPerson2);
        user2.removeFriend(idOfPerson1);

        log.info("Users {} and {} are no longer friends", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }

    public List<User> getFriendsListOfPerson(int id) {
        User user = userStorage.getUser(id)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + id + " not found"));

        log.info("The list of friends of the user with the id: {}", id);
        return user.getFriends().stream()
                .map(friendId -> userStorage.getUser(friendId)
                        .orElseThrow(() -> new ObjectNotFoundException("Friend with id: " + friendId + " not found")))
                .collect(Collectors.toList());
    }

    public List<User> getListOfCommonFriends(int idOfPerson1, int idOfPerson2) {
        User firstPerson = userStorage.getUser(idOfPerson1)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + idOfPerson1 + " not found"));
        User secondPerson = userStorage.getUser(idOfPerson2)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + idOfPerson2 + " not found"));

        log.info("A list of mutual friends of users with an id: {} и {}", idOfPerson1, idOfPerson2);

        return firstPerson.getFriends().stream()
                .filter(secondPerson.getFriends()::contains)
                .map(friendId -> userStorage.getUser(friendId)
                        .orElseThrow(() -> new ObjectNotFoundException("Friend with id: " + friendId + " not found")))
                .collect(Collectors.toList());
    }
}