package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;
    private InMemoryUserStorage userStorage;

    @BeforeEach
    void setUp() {
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);

        User user = new User();
        user.setLogin("testUser");
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        userStorage.createUser(user);
    }

    @Test
    void createFilmShouldAddFilm() {
        Film film = createTestFilm("Inception", "A mind-bending thriller", LocalDate.of(2010, 7, 16), 148);

        Film createdFilm = filmController.createFilm(film);

        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("Inception", createdFilm.getName());
    }

    @Test
    void createFilmShouldThrowExceptionForInvalidReleaseDate() {
        Film invalidFilm = createTestFilm("Old Film", "Invalid release date",
                LocalDate.of(1800, 1, 1), 120);

        ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(invalidFilm));
        assertEquals("The release date cannot be earlier 1895-12-28", exception.getMessage());
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = createTestFilm("Inception", "A mind-bending thriller",
                LocalDate.of(2010, 7, 16), 148);
        Film createdFilm = filmController.createFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getId());
        updatedFilm.setName("Interstellar");
        updatedFilm.setDescription("A space exploration epic");
        updatedFilm.setReleaseDate(LocalDate.of(2014, 11, 7));
        updatedFilm.setDuration(169);

        Film result = filmController.updateFilm(updatedFilm);

        assertEquals("Interstellar", result.getName());
        assertEquals("A space exploration epic", result.getDescription());
        assertEquals(LocalDate.of(2014, 11, 7), result.getReleaseDate());
    }

    @Test
    void updateFilmShouldThrowExceptionIfFilmNotFound() {
        Film nonExistentFilm = new Film();
        nonExistentFilm.setId(999); // Несуществующий ID
        nonExistentFilm.setName("Non-existent Film");
        nonExistentFilm.setDescription("Film not found");
        nonExistentFilm.setReleaseDate(LocalDate.of(2020, 1, 1));
        nonExistentFilm.setDuration(100);

        ObjectNotFoundException exception = assertThrows(
                ObjectNotFoundException.class,
                () -> filmController.updateFilm(nonExistentFilm)
        );

        assertEquals("Element with id = 999 not found", exception.getMessage());
    }

    @Test
    void getFilmShouldReturnFilmById() {
        Film film = createTestFilm("Inception", "A mind-bending thriller",
                LocalDate.of(2010, 7, 16), 148);
        Film createdFilm = filmController.createFilm(film);

        Film fetchedFilm = filmController.getFilm(createdFilm.getId());

        assertEquals("Inception", fetchedFilm.getName());
    }

    @Test
    void deleteFilmShouldRemoveFilm() {
        Film film = createTestFilm("Inception", "To be deleted",
                LocalDate.of(2010, 7, 16), 148);
        Film createdFilm = filmController.createFilm(film);

        Film deletedFilm = filmController.deleteFilm(createdFilm.getId());

        assertEquals(createdFilm.getId(), deletedFilm.getId());
        assertThrows(ObjectNotFoundException.class, () -> filmController.getFilm(createdFilm.getId()));
    }

    @Test
    void likeTheFilmShouldAddLike() {
        Film film = filmController.createFilm(createTestFilm("Inception", "Like test",
                LocalDate.of(2010, 7, 16), 148));
        int userId = 1;

        Film likedFilm = filmController.likeTheFilm(film.getId(), userId);

        assertTrue(likedFilm.getUsersLikes().contains(userId));
    }

    @Test
    void removeLikeShouldRemoveLike() {
        Film film = filmController.createFilm(createTestFilm("Inception", "Like removal test",
                LocalDate.of(2010, 7, 16), 148));
        int userId = 1;

        filmController.likeTheFilm(film.getId(), userId);
        Film unlikedFilm = filmController.removeLike(film.getId(), userId);

        assertFalse(unlikedFilm.getUsersLikes().contains(userId));
    }

    @Test
    void getTopOfFilmsShouldReturnMostLikedFilms() {
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);
        Film createdFilm1 = filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Interstellar");
        film2.setDescription("A space exploration epic");
        film2.setReleaseDate(LocalDate.of(2014, 11, 7));
        film2.setDuration(169);
        Film createdFilm2 = filmController.createFilm(film2);

        User user2 = new User();
        user2.setLogin("secondUser");
        user2.setName("Second User");
        user2.setEmail("second@example.com");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        userStorage.createUser(user2);

        int userId1 = 1;
        int userId2 = 2;
        filmController.likeTheFilm(createdFilm1.getId(), userId1);
        filmController.likeTheFilm(createdFilm2.getId(), userId1);
        filmController.likeTheFilm(createdFilm2.getId(), userId2);

        List<Film> topFilms = filmController.getTopOfFilms(1);

        assertEquals(1, topFilms.size());
        assertEquals("Interstellar", topFilms.getFirst().getName());
    }

    @Test
    void getAllFilmsShouldReturnAllAddedFilms() {
        filmController.createFilm(createTestFilm("Inception", "Thriller",
                LocalDate.of(2010, 7, 16), 148));
        filmController.createFilm(createTestFilm("Interstellar", "Epic",
                LocalDate.of(2014, 11, 7), 169));

        Collection<Film> allFilms = filmController.getAllFilms();

        assertEquals(2, allFilms.size());
    }

    private Film createTestFilm(String name, String description, LocalDate releaseDate, int duration) {
        Film film = new Film();
        film.setName(name);
        film.setDescription(description);
        film.setReleaseDate(releaseDate);
        film.setDuration(duration);
        return film;
    }
}