package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
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
        User user = createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("user1", createdUser.getLogin());
        assertEquals("User One", createdUser.getName());
    }

    @Test
    void createUserShouldThrowExceptionForInvalidEmail() {
        User user = createTestUser("invalid-email", "user1", "User One", LocalDate.of(1990, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Invalid email format.", exception.getMessage());
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        User user = createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setEmail("updated@example.com");
        updatedUser.setLogin("updatedLogin");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthday(LocalDate.of(1985, 5, 15));

        User result = userController.updateUser(updatedUser);

        assertEquals(createdUser.getId(), result.getId());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("updatedLogin", result.getLogin());
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void updateUserShouldThrowExceptionIfUserNotFound() {
        User nonExistentUser = createTestUser("user@example.com", "nonExistent", "Non-existent User", LocalDate.of(1990, 1, 1));
        nonExistentUser.setId(999);

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> userController.updateUser(nonExistentUser)
        );

        assertEquals("Element with id = 999 not found", exception.getMessage());
    }

    @Test
    void getUserShouldReturnUserById() {
        User user = createTestUser("user@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        User fetchedUser = userController.getUser(createdUser.getId());

        assertNotNull(fetchedUser);
        assertEquals(createdUser.getId(), fetchedUser.getId());
        assertEquals("user1", fetchedUser.getLogin());
    }

    @Test
    void getUserShouldThrowExceptionIfUserNotFound() {
        assertThrows(ObjectNotFoundException.class, () -> userController.getUser(999));
    }

    @Test
    void deleteUserShouldRemoveUser() {
        User user = createTestUser("user@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        User createdUser = userController.createUser(user);

        User deletedUser = userController.deleteUser(createdUser.getId());

        assertNotNull(deletedUser);
        assertEquals(createdUser.getId(), deletedUser.getId());
        assertThrows(ObjectNotFoundException.class, () -> userController.getUser(createdUser.getId()));
    }

    @Test
    void getAllUsersShouldReturnAllUsers() {
        User user1 = createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1));
        User user2 = createTestUser("user2@example.com", "user2", "User Two", LocalDate.of(1992, 2, 2));
        userController.createUser(user1);
        userController.createUser(user2);

        Collection<User> users = userController.getAllUsers();

        assertEquals(2, users.size());
    }

    @Test
    void addFriendsShouldEstablishFriendship() {
        User user1 = userController.createUser(createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1)));
        User user2 = userController.createUser(createTestUser("user2@example.com", "user2", "User Two", LocalDate.of(1992, 2, 2)));

        List<User> friends = userController.addFriends(user1.getId(), user2.getId());

        assertEquals(2, friends.size());
        assertTrue(user1.getFriends().contains(user2.getId()));
        assertTrue(user2.getFriends().contains(user1.getId()));
    }

    @Test
    void deleteFriendsShouldRemoveFriendship() {
        User user1 = userController.createUser(createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1)));
        User user2 = userController.createUser(createTestUser("user2@example.com", "user2", "User Two", LocalDate.of(1992, 2, 2)));
        userController.addFriends(user1.getId(), user2.getId());

        List<User> friends = userController.deleteFriends(user1.getId(), user2.getId());

        assertEquals(2, friends.size());
        assertFalse(user1.getFriends().contains(user2.getId()));
        assertFalse(user2.getFriends().contains(user1.getId()));
    }

    @Test
    void getFriendsListOfPersonShouldReturnFriendsList() {
        User user1 = userController.createUser(createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1)));
        User user2 = userController.createUser(createTestUser("user2@example.com", "user2", "User Two", LocalDate.of(1992, 2, 2)));
        userController.addFriends(user1.getId(), user2.getId());

        List<User> friendsList = userController.getFriendsListOfPerson(user1.getId());

        assertEquals(1, friendsList.size());
        assertEquals("user2", friendsList.getFirst().getLogin());
    }

    @Test
    void getListOfCommonFriendsShouldReturnCommonFriends() {
        User user1 = userController.createUser(createTestUser("user1@example.com", "user1", "User One", LocalDate.of(1990, 1, 1)));
        User user2 = userController.createUser(createTestUser("user2@example.com", "user2", "User Two", LocalDate.of(1992, 2, 2)));
        User commonFriend = userController.createUser(createTestUser("common@example.com", "common", "Common Friend", LocalDate.of(1995, 5, 5)));

        userController.addFriends(user1.getId(), commonFriend.getId());
        userController.addFriends(user2.getId(), commonFriend.getId());

        List<User> commonFriends = userController.getListOfCommonFriends(user1.getId(), user2.getId());

        assertEquals(1, commonFriends.size());
        assertEquals("common", commonFriends.getFirst().getLogin());
    }

    private User createTestUser(String email, String login, String name, LocalDate birthday) {
        User user = new User();
        user.setEmail(email);
        user.setLogin(login);
        user.setName(name);
        user.setBirthday(birthday);
        return user;
    }
}