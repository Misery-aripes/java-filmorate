package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(int id) {
        Film film = filmStorage.getFilm(id)
                .orElseThrow(() -> new ObjectNotFoundException("Movie with id: " + id + " not found"));
        filmStorage.deleteFilm(id);
        log.info("Фильм с id = {} был удален", id);
        return film;
    }

    public Film getFilm(int id) {
        return filmStorage.getFilm(id)
                .orElseThrow(() -> new ObjectNotFoundException("Movie with id: " + id + " not found"));
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film likeTheFilm(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId)
                .orElseThrow(() -> new ObjectNotFoundException("Movie with id: " + filmId + " not found"));

        userStorage.getUser(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User with id: " + userId + " not found"));

        film.addLike(userId);
        log.info("User with id: {} liked the movie with an id: {}", userId, filmId);
        return film;
    }

    public Film removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilm(filmId)
                .orElseThrow(() -> new ObjectNotFoundException("Movie with id: " + filmId + " not found"));

        if (userStorage.getUser(userId).isEmpty()) {
            throw new ObjectNotFoundException("User with id: " + userId + " not found");
        }

        film.removeLike(userId);
        log.info("A like from a user with id: {} was deleted from a movie with id: {}", userId, filmId);
        return film;
    }

    public List<Film> getTopOfFilms(Integer count) {
        log.info("Getting top: {} movies by likes", count);
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getUsersLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }
}