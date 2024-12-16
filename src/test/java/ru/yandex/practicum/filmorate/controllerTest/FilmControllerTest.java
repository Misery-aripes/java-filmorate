package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

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
        userStorage.addUser(user);
    }

    @Test
    void createFilmShouldAddFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        Film createdFilm = filmController.createFilm(film);

        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("Inception", createdFilm.getName());
    }

    @Test
    void createFilmShouldThrowValidationExceptionIfInvalid() {
        Film invalidFilm = new Film();
        invalidFilm.setName(""); // Invalid name
        invalidFilm.setReleaseDate(LocalDate.of(1800, 1, 1)); // Invalid release date
        invalidFilm.setDuration(-10); // Invalid duration

        assertThrows(Exception.class, () -> filmController.createFilm(invalidFilm));
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getId());
        updatedFilm.setName("Interstellar");
        updatedFilm.setDescription("A space exploration epic");
        updatedFilm.setReleaseDate(LocalDate.of(2014, 11, 7));
        updatedFilm.setDuration(169);

        Film result = filmController.updateFilm(updatedFilm);

        assertEquals(createdFilm.getId(), result.getId());
        assertEquals("Interstellar", result.getName());
        assertEquals("A space exploration epic", result.getDescription());
    }

    @Test
    void updateFilmShouldThrowExceptionIfFilmNotFound() {
        Film nonExistentFilm = new Film();
        nonExistentFilm.setId(999); // Non-existent ID
        nonExistentFilm.setName("Non-existent film");

        assertThrows(ObjectNotFoundException.class, () -> filmController.updateFilm(nonExistentFilm));
    }

    @Test
    void getFilmShouldReturnFilmById() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        Film fetchedFilm = filmController.getFilm(createdFilm.getId());

        assertNotNull(fetchedFilm);
        assertEquals("Inception", fetchedFilm.getName());
    }

    @Test
    void getFilmShouldThrowExceptionIfNotFound() {
        assertThrows(ObjectNotFoundException.class, () -> filmController.getFilm(999));
    }

    @Test
    void getAllFilmsShouldReturnAllFilms() {
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);
        filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Interstellar");
        film2.setDescription("A space exploration epic");
        film2.setReleaseDate(LocalDate.of(2014, 11, 7));
        film2.setDuration(169);
        filmController.createFilm(film2);

        Collection<Film> allFilms = filmController.getAllFilms();

        assertEquals(2, allFilms.size());
    }

    @Test
    void deleteFilmShouldRemoveFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        Film deletedFilm = filmController.deleteFilm(createdFilm.getId());

        assertEquals(createdFilm.getId(), deletedFilm.getId());
        assertThrows(ObjectNotFoundException.class, () -> filmController.getFilm(createdFilm.getId()));
    }

    @Test
    void likeAndUnlikeFilmShouldUpdateLikes() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);
        Film createdFilm = filmController.createFilm(film);

        int userId = 1;
        filmController.likeTheFilm(createdFilm.getId(), userId);

        Film likedFilm = filmController.getFilm(createdFilm.getId());
        assertTrue(likedFilm.getLikes().contains(userId));

        filmController.removeLike(createdFilm.getId(), userId);

        Film unlikedFilm = filmController.getFilm(createdFilm.getId());
        assertFalse(unlikedFilm.getLikes().contains(userId));
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

        int userId1 = 1;
        int userId2 = 2;
        User user2 = new User();
        user2.setLogin("secondUser");
        user2.setName("Second User");
        user2.setEmail("second@example.com");
        user2.setBirthday(LocalDate.of(1992, 2, 2));
        userStorage.addUser(user2);

        filmController.likeTheFilm(createdFilm1.getId(), userId1);
        filmController.likeTheFilm(createdFilm2.getId(), userId1);
        filmController.likeTheFilm(createdFilm2.getId(), userId2);

        List<Film> topFilms = filmController.getTopOfFilms(1);

        assertEquals(1, topFilms.size());
        assertEquals("Interstellar", topFilms.get(0).getName());
    }
}