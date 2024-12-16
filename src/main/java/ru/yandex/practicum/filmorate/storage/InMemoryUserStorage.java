package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int nextId = 1;

    @Override
    public User addUser(User user) {
        validateUser(user);
        user.setId(nextId++);
        users.put(user.getId(), user);
        log.info("Был добавлен пользователь с id = {}, имя = {}", user.getId(), user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        validateUser(user);
        if (!users.containsKey(user.getId())) {
            throw new IllegalArgumentException("Пользователь с id " + user.getId() + " не найден.");
        }
        users.put(user.getId(), user);
        log.info("Информация о пользователе с id = {} была обновлена", user.getId());
        return user;
    }

    @Override
    public Optional<User> getUserById(int id) {
        log.info("Поиск пользователя с id = {}", id);
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Получен список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public void deleteUserById(int id) {
        if (!users.containsKey(id)) {
            throw new ObjectNotFoundException("Пользователь с id " + id + " не найден.");
        }
        log.info("Пользователь с id = {} был удалён", id);
        users.remove(id);
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}