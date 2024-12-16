package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Slf4j
@Component
public class InMemoryFilmStorage extends InMemoryStorage<Film> implements FilmStorage {

    @Override
    public Film createFilm(Film film) {
        log.info("The movie {} has been added", film.getName());
        return create(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.info("Updating film with id = {}", film.getId());
        return update(film); // Логика в базовом классе
    }

    @Override
    public Film deleteFilm(int id) {
        return delete(id)
                .orElseThrow(() -> new ObjectNotFoundException("The movie with ID " + id + " was not found"));
    }

    @Override
    protected void setId(Film film, int id) {
        film.setId(id);
    }

    @Override
    protected int getId(Film film) {
        return film.getId();
    }

    @Override
    public Optional<Film> getFilm(int id) {
        return get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return getAll().values();
    }
}