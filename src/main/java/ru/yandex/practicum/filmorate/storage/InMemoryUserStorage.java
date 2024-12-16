package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage extends InMemoryStorage<User> implements UserStorage {

    @Override
    public User createUser(User user) {
        log.info("Adding a user: {}", user.getName());
        return create(user);
    }

    @Override
    public User updateUser(User user) {
        log.info("Updating a user with an id {}", user.getId());
        return update(user);
    }

    @Override
    public Optional<User> deleteUser(int id) {
        return delete(id)
                .or(() -> {
                    throw new ObjectNotFoundException("The user with the id " + id + " was not found");
                });
    }

    @Override
    protected void setId(User user, int id) {
        user.setId(id);
    }

    @Override
    protected int getId(User user) {
        return user.getId();
    }

    @Override
    public Optional<User> getUser(int id) {
        return get(id);
    }

    @Override
    public Collection<User> getAllUsers() {
        return getAll().values();
    }
}