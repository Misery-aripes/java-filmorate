package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
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

        // Добавляем пользователя для тестов лайков
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

        ResponseEntity<Film> response = filmController.createFilm(film);

        assertNotNull(response.getBody());
        assertEquals(201, response.getStatusCodeValue());
        assertEquals(1, response.getBody().getId());
        assertEquals("Inception", response.getBody().getName());
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ResponseEntity<Film> createdFilm = filmController.createFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getBody().getId());
        updatedFilm.setName("Interstellar");
        updatedFilm.setDescription("A space exploration epic");
        updatedFilm.setReleaseDate(LocalDate.of(2014, 11, 7));
        updatedFilm.setDuration(169);

        ResponseEntity<Film> response = filmController.updateFilm(updatedFilm);

        assertNotNull(response.getBody());
        assertEquals("Interstellar", response.getBody().getName());
    }

    @Test
    void getFilmShouldReturnFilmById() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ResponseEntity<Film> createdFilm = filmController.createFilm(film);
        ResponseEntity<Film> response = filmController.getFilmById(createdFilm.getBody().getId());

        assertNotNull(response.getBody());
        assertEquals("Inception", response.getBody().getName());
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

        ResponseEntity<List<Film>> response = filmController.getAllFilms();

        assertEquals(2, response.getBody().size());
    }

    @Test
    void addAndRemoveLikeShouldUpdateLikes() {
        Film film = new Film();
        film.setName("Inception");
        film.setDescription("A mind-bending thriller");
        film.setReleaseDate(LocalDate.of(2010, 7, 16));
        film.setDuration(148);

        ResponseEntity<Film> createdFilm = filmController.createFilm(film);
        int filmId = createdFilm.getBody().getId();
        int userId = 1;

        // Добавляем лайк
        filmController.addLike(filmId, userId);
        ResponseEntity<Film> filmWithLike = filmController.getFilmById(filmId);
        assertTrue(filmWithLike.getBody().getLikes().contains(userId));

        // Удаляем лайк
        filmController.removeLike(filmId, userId);
        ResponseEntity<Film> filmWithoutLike = filmController.getFilmById(filmId);
        assertFalse(filmWithoutLike.getBody().getLikes().contains(userId));
    }

    @Test
    void getPopularFilmsShouldReturnMostLikedFilms() {
        Film film1 = new Film();
        film1.setName("Inception");
        film1.setDescription("A mind-bending thriller");
        film1.setReleaseDate(LocalDate.of(2010, 7, 16));
        film1.setDuration(148);
        ResponseEntity<Film> createdFilm1 = filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Interstellar");
        film2.setDescription("A space exploration epic");
        film2.setReleaseDate(LocalDate.of(2014, 11, 7));
        film2.setDuration(169);
        ResponseEntity<Film> createdFilm2 = filmController.createFilm(film2);

        int userId = 1;
        filmController.addLike(createdFilm2.getBody().getId(), userId);

        ResponseEntity<List<Film>> response = filmController.getPopularFilms(1);

        assertEquals(1, response.getBody().size());
        assertEquals("Interstellar", response.getBody().get(0).getName());
    }
}