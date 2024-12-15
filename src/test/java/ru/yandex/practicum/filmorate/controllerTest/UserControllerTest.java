package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    void shouldCreateUserSuccessfully() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("user");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = userController.createUser(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
        assertEquals("user@yandex.ru", createdUser.getEmail());
        assertEquals("user", createdUser.getLogin());
        assertEquals("user", createdUser.getName());
        assertEquals(LocalDate.of(2000, 1, 1), createdUser.getBirthday());
    }

    @Test
    void shouldThrowValidationExceptionWhenEmailIsInvalid() {
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.",
                exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("invalid login");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void shouldThrowValidationExceptionWhenBirthdayIsInFuture() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> userController.createUser(user));
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User user = new User();
        user.setEmail("user@yandex.ru");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User createdUser = userController.createUser(user);

        User updatedUser = new User();
        updatedUser.setId(createdUser.getId());
        updatedUser.setEmail("updated@yandex.ru");
        updatedUser.setLogin("updatedLogin");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthday(LocalDate.of(1995, 5, 5));

        User result = userController.updateUser(updatedUser);

        assertNotNull(result);
        assertEquals(createdUser.getId(), result.getId());
        assertEquals("updated@yandex.ru", result.getEmail());
        assertEquals("updatedLogin", result.getLogin());
        assertEquals("Updated Name", result.getName());
        assertEquals(LocalDate.of(1995, 5, 5), result.getBirthday());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {
        User user = new User();
        user.setId(999);
        user.setEmail("user@yandex.ru");
        user.setLogin("validLogin");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userController.updateUser(user));
        assertEquals("Пользователь с id 999 не найден.", exception.getMessage());
    }

    @Test
    void shouldReturnAllUsers() {
        User user1 = new User();
        user1.setEmail("user1@yandex.ru");
        user1.setLogin("user1");
        user1.setBirthday(LocalDate.of(2000, 1, 1));
        userController.createUser(user1);

        User user2 = new User();
        user2.setEmail("user2@yandex.ru");
        user2.setLogin("user2");
        user2.setBirthday(LocalDate.of(1995, 5, 5));
        userController.createUser(user2);

        List<User> users = userController.getAllUsers();

        assertEquals(2, users.size());
        assertEquals("user1@yandex.ru", users.get(0).getEmail());
        assertEquals("user2@yandex.ru", users.get(1).getEmail());
    }
}
