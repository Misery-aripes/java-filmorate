package ru.yandex.practicum.filmorate.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;

    @BeforeEach
    void setUp() {
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        FilmService filmService = new FilmService(filmStorage, userStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void createFilmShouldAddFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);

        Film createdFilm = filmController.createFilm(film);

        assertNotNull(createdFilm);
        assertEquals(1, createdFilm.getId());
        assertEquals("Test Film", createdFilm.getName());
    }

    @Test
    void updateFilmShouldUpdateExistingFilm() {
        Film film = new Film();
        film.setName("Initial Film");
        film.setDescription("Initial description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Film createdFilm = filmController.createFilm(film);

        Film updatedFilm = new Film();
        updatedFilm.setId(createdFilm.getId());
        updatedFilm.setName("Updated Film");
        updatedFilm.setDescription("Updated description");
        updatedFilm.setReleaseDate(LocalDate.of(2021, 1, 1));
        updatedFilm.setDuration(150);

        Film result = filmController.updateFilm(updatedFilm);

        assertEquals("Updated Film", result.getName());
        assertEquals("Updated description", result.getDescription());
    }

    @Test
    void getFilmByIdShouldReturnCorrectFilm() {
        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Test description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Film createdFilm = filmController.createFilm(film);

        Film fetchedFilm = filmController.getFilmById(createdFilm.getId());

        assertNotNull(fetchedFilm);
        assertEquals(createdFilm.getName(), fetchedFilm.getName());
    }

    @Test
    void getAllFilmsShouldReturnAllFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Description 1");
        film1.setReleaseDate(LocalDate.of(2020, 1, 1));
        film1.setDuration(100);
        filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Description 2");
        film2.setReleaseDate(LocalDate.of(2021, 1, 1));
        film2.setDuration(120);
        filmController.createFilm(film2);

        Collection<Film> allFilms = filmController.getAllFilms();

        assertEquals(2, allFilms.size());
    }

    @Test
    void addLikeShouldIncreaseLikes() {
        Film film = new Film();
        film.setName("Film With Like");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Film createdFilm = filmController.createFilm(film);

        filmController.addLike(createdFilm.getId(), 1);

        assertTrue(createdFilm.getLikes().contains(1));
    }

    @Test
    void removeLikeShouldDecreaseLikes() {
        Film film = new Film();
        film.setName("Film With Like");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(2020, 1, 1));
        film.setDuration(120);
        Film createdFilm = filmController.createFilm(film);

        filmController.addLike(createdFilm.getId(), 1);
        filmController.removeLike(createdFilm.getId(), 1);

        assertFalse(createdFilm.getLikes().contains(1));
    }

    @Test
    void getPopularFilmsShouldReturnTopFilms() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setReleaseDate(LocalDate.of(2020, 1, 1));
        film1.setDuration(120);
        Film createdFilm1 = filmController.createFilm(film1);

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setReleaseDate(LocalDate.of(2021, 1, 1));
        film2.setDuration(120);
        Film createdFilm2 = filmController.createFilm(film2);

        filmController.addLike(createdFilm2.getId(), 1);

        List<Film> topFilms = filmController.getPopularFilms(1);

        assertEquals(1, topFilms.size());
        assertEquals("Film 2", topFilms.getFirst().getName());
    }
}