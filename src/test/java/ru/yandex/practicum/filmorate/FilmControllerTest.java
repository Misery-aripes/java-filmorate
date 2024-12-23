package ru.yandex.practicum.filmorate;

import jakarta.validation.ValidatorFactory;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

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
    void shouldCreateValidFilm() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(0, violations.size(), "No validation errors expected for valid film.");
    }

    @Test
    void shouldFailToCreateEmptyFilm() {
        Film film = Film.builder().build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertFalse(violations.isEmpty(), "Validation errors expected for empty film.");
    }

    @Test
    void shouldFailWhenNameIsNull() {
        Film film = Film.builder()
                .name(null)
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "name");
    }

    @Test
    void shouldFailWhenNameIsBlank() {
        Film film = Film.builder()
                .name(" ")
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "name");
    }

    @Test
    void shouldFailWhenNameIsEmpty() {
        Film film = Film.builder()
                .name("")
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "name");
    }

    @Test
    void shouldFailWhenDescriptionIsNull() {
        Film film = Film.builder()
                .name("Test Film")
                .description(null)
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "description");
    }

    @Test
    void shouldFailWhenDescriptionIsBlank() {
        Film film = Film.builder()
                .name("Test Film")
                .description(" ")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "description");
    }

    @Test
    void shouldFailWhenDescriptionIsEmpty() {
        Film film = Film.builder()
                .name("Test Film")
                .description("")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "description");
    }

    @Test
    void shouldFailWhenDescriptionIsTooLong() {
        Film film = Film.builder()
                .name("Test Film")
                .description("O".repeat(300))
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .build();

        assertViolation(film, "description");
    }

    @Test
    void shouldFailWhenReleaseDateIsTooEarly() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.of(1895, 12, 27))
                .duration(100)
                .build();

        assertViolation(film, "releaseDate");
    }

    @Test
    void shouldFailWhenDurationIsNegative() {
        Film film = Film.builder()
                .name("Test Film")
                .description("Test description")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(-100)
                .build();

        assertViolation(film, "duration");
    }

    private void assertViolation(Film film, String fieldName) {
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        assertFalse(violations.isEmpty(), "Expected validation error for field: " + fieldName);
        assertEquals(1, violations.stream()
                .filter(v -> v.getPropertyPath().toString().equals(fieldName))
                .count(), "Validation error not found for field: " + fieldName);
    }
}