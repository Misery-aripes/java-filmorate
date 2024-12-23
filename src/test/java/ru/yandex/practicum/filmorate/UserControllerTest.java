package ru.yandex.practicum.filmorate;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class UserControllerTest {

    private ValidatorFactory validatorFactory;
    private Validator validator;

    @BeforeEach
    void setUp() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    void tearDown() {
        validatorFactory.close();
    }

    @Test
    void shouldCreateValidUser() {
        User user = User.builder()
                .name("Test User")
                .email("test@yandex.ru")
                .login("testUser")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(0, violations.size(), "No validation errors expected for valid user.");
    }

    @Test
    void shouldFailToCreateEmptyUser() {
        User user = User.builder().build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation errors expected for empty user.");
    }

    @Test
    void shouldFailWithInvalidEmail() {
        User user = User.builder()
                .email("testemail.com@")
                .login("testUser")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertViolation(user, "email");
    }

    @Test
    void shouldFailWithNullEmail() {
        User user = User.builder()
                .email(null)
                .login("testUser")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertViolation(user, "email");
    }

    @Test
    void shouldFailWithNullLogin() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login(null)
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertViolation(user, "login");
    }

    @Test
    void shouldFailWithBlankLogin() {
        User user = User.builder()
                .email("testet@yandex.ru")
                .login(" ")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(2, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("login"))
                .count());
    }

    @Test
    void shouldFailWithEmptyLogin() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation errors expected.");
        assertEquals(2, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals("login"))
                .count(), "Expected multiple validation errors for field: login");
    }

    @Test
    void shouldFailWithSpaceInLogin() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("test User")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        assertViolation(user, "login");
    }

    @Test
    void shouldFailWithFutureBirthday() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("testUser")
                .name("Test User")
                .birthday(LocalDate.now().plusYears(1))
                .build();

        assertViolation(user, "birthday");
    }

    @Test
    void shouldFailWithNullBirthday() {
        User user = User.builder()
                .email("test@yandex.ru")
                .login("testUser")
                .name("Test User")
                .birthday(null)
                .build();

        assertViolation(user, "birthday");
    }

    private void assertViolation(User user, String fieldName) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Validation errors expected.");
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName))
                .count(), "Validation error not found for field: " + fieldName);
    }
}