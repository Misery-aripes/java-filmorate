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
        log.debug("Создание нового пользователя: {}", user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.debug("Обновление пользователя с id = {}", user.getId());
        return userStorage.updateUser(user);
    }

    public User getUser(int id) {
        log.debug("Получение пользователя с id = {}", id);
        return userStorage.getUserById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь с id = " + id + " не найден"));
    }

    public User deleteUser(int id) {
        User user = getUser(id); // Проверяем, существует ли пользователь
        userStorage.deleteUserById(id);
        log.info("Пользователь с id = {} был удалён", id);
        return user;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

/*    public List<User> addFriends(int idOfPerson1, int idOfPerson2) {
        User user1 = getUser(idOfPerson1);
        User user2 = getUser(idOfPerson2);

        if (user1.getFriends().contains(idOfPerson2)) {
            throw new InternalServerException("Пользователи уже являются друзьями");
        }

        user1.getFriends().add(idOfPerson2);
        user2.getFriends().add(idOfPerson1);

        log.info("Пользователи с id: {} и {} теперь друзья", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }*/

    public List<User> addFriends(int idOfPerson1, int idOfPerson2) {
        // Проверяем, существуют ли оба пользователя
        User user1 = getUser(idOfPerson1);
        User user2 = getUser(idOfPerson2);

        if (user1.getFriends().contains(idOfPerson2)) {
            throw new InternalServerException("Пользователи уже являются друзьями");
        }

        user1.getFriends().add(idOfPerson2);
        user2.getFriends().add(idOfPerson1);

        log.info("Пользователи с id: {} и {} теперь друзья", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }

    public List<User> deleteFriends(int idOfPerson1, int idOfPerson2) {
        User user1 = getUser(idOfPerson1);
        User user2 = getUser(idOfPerson2);

        user1.getFriends().remove(idOfPerson2);
        user2.getFriends().remove(idOfPerson1);

        log.info("Пользователи {} и {} больше не друзья", idOfPerson1, idOfPerson2);
        return List.of(user1, user2);
    }

    public List<User> getFriendsListOfPerson(int id) {
        User user = getUser(id);

        log.info("Список друзей пользователя с id: {}", id);
        return user.getFriends().stream()
                .map(friendId -> getUser(friendId)) // Используем метод getUser для проверки существования друга
                .collect(Collectors.toList());
    }

    public List<User> getListOfCommonFriends(int idOfPerson1, int idOfPerson2) {
        User firstPerson = getUser(idOfPerson1);
        User secondPerson = getUser(idOfPerson2);

        log.info("Список общих друзей пользователей с id: {} и {}", idOfPerson1, idOfPerson2);

        return firstPerson.getFriends().stream()
                .filter(secondPerson.getFriends()::contains)
                .map(this::getUser) // Используем метод getUser для проверки существования друга
                .collect(Collectors.toList());
    }
}
