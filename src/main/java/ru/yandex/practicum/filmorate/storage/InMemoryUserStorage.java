package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public User addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Added new user with id {}", user.getId());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("User with id " + user.getId() + " not found.");
        }
        users.put(user.getId(), user);
        log.info("Updated user with id {}", user.getId());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Fetching all users. Total users: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
        User user = Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NotFoundException("User with id " + id + " not found."));
        log.info("Fetched user with id {}", id);
        return user;
    }
}
