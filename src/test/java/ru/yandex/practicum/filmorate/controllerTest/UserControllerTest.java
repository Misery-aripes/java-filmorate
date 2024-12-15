package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        userController = new UserController(userService);
    }

    @Test
    void createUserShouldAddUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("testLogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
    }

    @Test
    void getUserByIdShouldReturnUser() {
        User user = new User();
        user.setEmail("test@mail.com");
        user.setLogin("testLogin");
        user.setName("Test User");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        User createdUser = userController.createUser(user);

        User fetchedUser = userController.getUserById(createdUser.getId());

        assertNotNull(fetchedUser);
        assertEquals(createdUser.getId(), fetchedUser.getId());
    }

    @Test
    void addFriendShouldAddFriendToUser() {
        User user1 = new User();
        user1.setEmail("user1@mail.com");
        user1.setLogin("user1");
        userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@mail.com");
        user2.setLogin("user2");
        userController.createUser(user2);

        userController.addFriend(1, 2);

        assertTrue(user1.getFriends().contains(2));
    }
}