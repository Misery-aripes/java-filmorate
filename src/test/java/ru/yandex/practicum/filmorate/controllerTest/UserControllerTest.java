package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.List;

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
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        var response = userController.createUser(user);
        User createdUser = response.getBody();

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("user1", createdUser.getLogin());
    }

    @Test
    void updateUserShouldUpdateExistingUser() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        var createdResponse = userController.createUser(user);
        User createdUser = createdResponse.getBody();

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setEmail("updated_user@example.com");
        updatedUser.setLogin("updated_user");
        updatedUser.setName("Updated User");
        updatedUser.setBirthday(LocalDate.of(1985, 5, 15));

        var updatedResponse = userController.updateUser(updatedUser);
        User result = updatedResponse.getBody();

        assertNotNull(result);
        assertEquals("updated_user", result.getLogin());
        assertEquals("Updated User", result.getName());
        assertEquals("updated_user@example.com", result.getEmail());
    }

    @Test
    void getUserByIdShouldReturnUser() {
        User user = new User();
        user.setEmail("user1@example.com");
        user.setLogin("user1");
        user.setName("User One");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        var createdResponse = userController.createUser(user);
        User createdUser = createdResponse.getBody();

        var fetchedResponse = userController.getUserById(createdUser.getId());
        User fetchedUser = fetchedResponse.getBody();

        assertNotNull(fetchedUser);
        assertEquals("user1", fetchedUser.getLogin());
        assertEquals("User One", fetchedUser.getName());
    }

    @Test
    void addFriendShouldEstablishFriendship() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        var createdUser1 = userController.createUser(user1).getBody();

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        var createdUser2 = userController.createUser(user2).getBody();

        userController.addFriend(createdUser1.getId(), createdUser2.getId());

        var friendsResponse = userController.getFriends(createdUser1.getId());
        List<User> friends = friendsResponse.getBody();

        assertNotNull(friends);
        assertEquals(1, friends.size());
        assertEquals("user2", friends.get(0).getLogin());
    }

    @Test
    void removeFriendShouldDeleteFriendship() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        var createdUser1 = userController.createUser(user1).getBody();

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        var createdUser2 = userController.createUser(user2).getBody();

        userController.addFriend(createdUser1.getId(), createdUser2.getId());
        userController.removeFriend(createdUser1.getId(), createdUser2.getId());

        var friendsResponse = userController.getFriends(createdUser1.getId());
        List<User> friends = friendsResponse.getBody();

        assertNotNull(friends);
        assertTrue(friends.isEmpty());
    }

    @Test
    void getCommonFriendsShouldReturnMutualFriends() {
        User user1 = new User();
        user1.setEmail("user1@example.com");
        user1.setLogin("user1");
        user1.setName("User One");
        user1.setBirthday(LocalDate.of(1990, 1, 1));
        var createdUser1 = userController.createUser(user1).getBody();

        User user2 = new User();
        user2.setEmail("user2@example.com");
        user2.setLogin("user2");
        user2.setName("User Two");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        var createdUser2 = userController.createUser(user2).getBody();

        User mutualFriend = new User();
        mutualFriend.setEmail("friend@example.com");
        mutualFriend.setLogin("friend");
        mutualFriend.setName("Mutual Friend");
        mutualFriend.setBirthday(LocalDate.of(1988, 8, 8));
        var createdFriend = userController.createUser(mutualFriend).getBody();

        userController.addFriend(createdUser1.getId(), createdFriend.getId());
        userController.addFriend(createdUser2.getId(), createdFriend.getId());

        var commonFriendsResponse = userController.getCommonFriends(createdUser1.getId(), createdUser2.getId());
        List<User> commonFriends = commonFriendsResponse.getBody();

        assertNotNull(commonFriends);
        assertEquals(1, commonFriends.size());
        assertEquals("friend", commonFriends.get(0).getLogin());
    }
}