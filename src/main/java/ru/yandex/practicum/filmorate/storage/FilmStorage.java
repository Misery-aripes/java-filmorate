package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Film addFilm(Film film);              // Добавление фильма
    Film updateFilm(Film film);           // Обновление фильма
    Optional<Film> getFilmById(int id);   // Получение фильма по id
    void deleteFilmById(int id);          // Удаление фильма по id
    List<Film> getAllFilms();             // Получение всех фильмов
}