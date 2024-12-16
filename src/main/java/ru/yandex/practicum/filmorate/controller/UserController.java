package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        validateUser(user);
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        validateUser(user);
        return userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public User deleteUser(@PathVariable int id) {
        validateId(id);
        return userService.deleteUser(id);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        validateId(id);
        return userService.getUser(id);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public List<User> addFriends(@PathVariable int id, @PathVariable int friendId) {
        validateId(id);
        validateId(friendId);
        return userService.addFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public List<User> deleteFriends(@PathVariable int id, @PathVariable int friendId) {
        validateId(id);
        validateId(friendId);
        return userService.deleteFriends(id, friendId);
    }

    @GetMapping("{id}/friends")
    public List<User> getFriendsListOfPerson(@PathVariable int id) {
        validateId(id);
        return userService.getFriendsListOfPerson(id);
    }

    @GetMapping("/{id}/friends/common/{friendId}")
    public List<User> getListOfCommonFriends(@PathVariable int id, @PathVariable int friendId) {
        validateId(id);
        validateId(friendId);
        return userService.getListOfCommonFriends(id, friendId);
    }

    private void validateUser(User user) {
        Objects.requireNonNull(user, "The user cannot be null");
        if (user.getLogin() == null || user.getLogin().isBlank()) {
            throw new ValidationException("The login cannot be empty or contain spaces.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Invalid email format.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("The date of birth cannot be in the future.");
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("The ID must be a positive number.");
        }
    }
}