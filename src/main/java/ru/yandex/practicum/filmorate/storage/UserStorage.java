package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    User addUser(User user);              // Добавление пользователя
    User updateUser(User user);           // Обновление пользователя
    Optional<User> getUserById(int id);   // Получение пользователя по id
    void deleteUserById(int id);          // Удаление пользователя по id
    List<User> getAllUsers();             // Получение всех пользователей
}