package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);

        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
    }

    public List<User> getCommonFriends(int userId, int otherUserId) {
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherUserId);

        Set<Integer> commonFriendIds = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());

        return commonFriendIds.stream()
                .map(userStorage::getUserById)
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId);
        return user.getFriends().stream()
                .map(userStorage::getUserById)
                .toList();
    }
}