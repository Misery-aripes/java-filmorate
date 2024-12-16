package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    // Добавление фильма
    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        log.info("Создание нового фильма: {}", film);
        return filmService.createFilm(film);
    }

    // Обновление фильма
    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        validateFilm(film);
        log.info("Обновление фильма: {}", film);
        return filmService.updateFilm(film);
    }

    // Удаление фильма
    @DeleteMapping("/{id}")
    public Film deleteFilm(@PathVariable int id) {
        log.info("Удаление фильма с id {}", id);
        return filmService.deleteFilm(id);
    }

    // Получение фильма по id
    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Получение фильма с id {}", id);
        return filmService.getFilm(id);
    }

    // Получение всех фильмов
    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получение всех фильмов");
        return filmService.getAllFilms();
    }

    // Добавление лайка
    @PutMapping("/{id}/like/{userId}")
    public Film likeTheFilm(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь {} ставит лайк фильму с id {}", userId, id);
        return filmService.likeTheFilm(id, userId);
    }

    // Удаление лайка
    @DeleteMapping("/{id}/like/{userId}")
    public Film removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Пользователь {} удаляет лайк с фильма с id {}", userId, id);
        return filmService.removeLike(id, userId);
    }

    // Получение топа популярных фильмов
    @GetMapping("/popular")
    public List<Film> getTopOfFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        if (count <= 0) {
            log.error("Параметр count должен быть положительным числом: {}", count);
            throw new IllegalArgumentException("Параметр count должен быть положительным числом.");
        }
        log.info("Получение топ-{} популярных фильмов", count);
        return filmService.getTopOfFilms(count);
    }

    // Валидация фильма
    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            log.error("Дата релиза не может быть раньше {}", FIRST_FILM_DATE);
            throw new ValidationException("Дата релиза не может быть раньше " + FIRST_FILM_DATE);
        }
        if (film.getName() == null || film.getName().trim().isEmpty()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма должна быть положительной: {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}