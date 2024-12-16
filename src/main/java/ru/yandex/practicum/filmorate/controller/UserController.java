package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Получен запрос на получение всех пользователей");
        return new ArrayList<>(userService.getAllUsers());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        log.info("Получен запрос на создание пользователя: {}", user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя с id: {}", user.getId());
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable int id) {
        log.info("Получен запрос на удаление пользователя с id: {}", id);
        return userService.deleteUser(id);
    }

    @PostMapping("/{id}/friends/{friendId}")
    public List<User> addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос на добавление друга: {} -> {}", id, friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("Получен запрос на удаление друга: {} -> {}", id, friendId);
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriendsList(@PathVariable int id) {
        log.info("Получен запрос на список друзей пользователя с id: {}", id);
        return userService.getFriendsListOfPerson(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Получен запрос на список общих друзей между пользователями с id: {} и {}", id, otherId);
        return userService.getListOfCommonFriends(id, otherId);
    }
}