package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int nextId = 1;

    @Override
    public Film addFilm(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("Film cannot be null.");
        }
        film.setId(nextId++);
        films.put(film.getId(), film);
        log.info("Added new film with id {}", film.getId());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film == null) {
            throw new IllegalArgumentException("Film cannot be null.");
        }
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Film with id " + film.getId() + " not found.");
        }
        films.put(film.getId(), film);
        log.info("Updated film with id {}", film.getId());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.info("Fetching all films. Total films: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        Film film = Optional.ofNullable(films.get(id))
                .orElseThrow(() -> new NotFoundException("Film with id " + id + " not found."));
        log.info("Fetched film with id {}", id);
        return film;
    }
}
