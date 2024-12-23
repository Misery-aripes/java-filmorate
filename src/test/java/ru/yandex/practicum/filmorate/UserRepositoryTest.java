package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipRepository;
import ru.yandex.practicum.filmorate.storage.UserRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserRepository.class,
        UserRowMapper.class,
        FriendshipRepository.class})
class UserRepositoryTest {

    private final UserRepository userRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    private static User user1;
    private static User user2;
    private static User user3;

    @BeforeAll
    static void setUpUsers() {
        user1 = User.builder()
                .email("test1@example.com")
                .login("TestUser1")
                .name("name1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        user2 = User.builder()
                .email("test2@example.com")
                .login("TestUser2")
                .name("name2")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        user3 = User.builder()
                .email("test3@example.com")
                .login("TestUser3")
                .name("name3")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
    }

    @Test
    void shouldReturnAllUsers() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        assertThat(userRepository.getUsers())
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    void shouldReturnUserById() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        User user = userRepository.getUserById(user1.getId());

        assertThat(user)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", user1.getId());
    }

    @Test
    void shouldCreateUsers() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        assertThat(user2)
                .hasFieldOrPropertyWithValue("id", user2.getId());
    }

    @Test
    void shouldUpdateUser() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        User updatedUser = User.builder()
                .id(user1.getId())
                .email("test@example.com")
                .login("UpdateUser")
                .name("Update User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User updated = userRepository.updateUser(updatedUser);

        assertThat(updated)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Update User");
    }

    @Test
    void shouldDeleteUser() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        userRepository.deleteUser(user1.getId());

        assertThrows(NotFoundException.class, () -> userRepository.getUserById(user1.getId()));
    }

    @Test
    void shouldReturnCommonFriends() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        friendshipRepository.addFriend(user1.getId(), user3.getId());
        friendshipRepository.addFriend(user1.getId(), user2.getId());
        friendshipRepository.addFriend(user2.getId(), user3.getId());

        assertThat(friendshipRepository.getCommonFriends(user1.getId(), user2.getId()))
                .contains(userRepository.getUserById(user3.getId()));
    }

    @Test
    void shouldAddFriend() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        friendshipRepository.addFriend(user1.getId(), user3.getId());

        Collection<User> users = friendshipRepository.getAllFriends(user1.getId());

        assertThat(users)
                .contains(userRepository.getUserById(user3.getId()));

        users = friendshipRepository.getAllFriends(user3.getId());

        assertThat(users)
                .doesNotContain(userRepository.getUserById(user1.getId()));
    }

    @Test
    void shouldDeleteFriend() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);

        friendshipRepository.addFriend(user1.getId(), user2.getId());
        userRepository.createUser(user3);

        friendshipRepository.deleteFriend(user1.getId(), user2.getId());

        Collection<User> users = friendshipRepository.getAllFriends(user1.getId());

        assertThat(users)
                .doesNotContain(userRepository.getUserById(user2.getId()));
    }

    @Test
    void shouldReturnAllFriends() {
        userRepository.createUser(user1);
        userRepository.createUser(user2);
        userRepository.createUser(user3);

        friendshipRepository.addFriend(user1.getId(), user2.getId());
        friendshipRepository.addFriend(user1.getId(), user3.getId());

        Collection<User> users = friendshipRepository.getAllFriends(user1.getId());

        assertThat(users)
                .containsAll(List.of(
                        userRepository.getUserById(user2.getId()),
                        userRepository.getUserById(user3.getId())
                ));
    }
}