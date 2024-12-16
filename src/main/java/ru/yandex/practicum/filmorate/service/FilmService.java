package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
    }

    public Film createFilm(Film film) {
        log.debug("Добавление нового фильма: {}", film);
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.debug("Обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    public Film deleteFilm(int id) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с id " + id + " не найден."));
        filmStorage.deleteFilmById(id);
        log.debug("Удаление фильма: {}", film);
        return film;
    }

    public Film getFilm(int id) {
        log.debug("Фильм с id {} получен", id);
        return filmStorage.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с id " + id + " не найден."));
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

/*    public Film likeTheFilm(int id, int userId) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с id " + id + " не найден."));
        if (!film.getLikes().add(userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму.");
        }
        return film;
    }

    public Film removeLike(int id, int userId) {
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с id " + id + " не найден."));
        if (!film.getLikes().remove(userId)) {
            throw new ValidationException("Пользователь не ставил лайк этому фильму.");
        }
        return film;
    }*/

    public Film likeTheFilm(int id, int userId) {
        // Проверяем, существует ли пользователь
        userService.getUser(userId);

        // Проверяем, существует ли фильм
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с id " + id + " не найден."));

        if (!film.getLikes().add(userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму.");
        }

        log.info("Пользователь с id {} поставил лайк фильму с id {}", userId, id);
        return film;
    }

    public Film removeLike(int id, int userId) {
        // Проверяем, существует ли пользователь
        userService.getUser(userId);

        // Проверяем, существует ли фильм
        Film film = filmStorage.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Фильм с id " + id + " не найден."));

        if (!film.getLikes().remove(userId)) {
            throw new ValidationException("Пользователь не ставил лайк этому фильму.");
        }

        log.info("Пользователь с id {} удалил лайк с фильма с id {}", userId, id);
        return film;
    }

    public List<Film> getTopOfFilms(int count) {
        log.debug("Получен список фильмов");
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .toList();
    }
}