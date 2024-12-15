package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;


@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public User createUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUserById(int id) {
        return Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new ObjectNotFoundException("User with id " + id + " not found."));
    }

    @Override
    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ObjectNotFoundException("User not found.");
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> getUser(int id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> deleteUser(int id) {
        return Optional.ofNullable(users.remove(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values()); // Преобразует в List
    }
}