package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(int id);

    Optional<User> getUser(int id);

    User createUser(User user);
}
