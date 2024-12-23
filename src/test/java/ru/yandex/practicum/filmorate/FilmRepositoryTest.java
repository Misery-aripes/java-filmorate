package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.mapper.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmRepository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({FilmRepository.class,
        FilmRowMapper.class})
class FilmRepositoryTest {

    private final FilmRepository filmRepository;
    private static Film film1;
    private static Film film2;
    private static Film film3;

    @BeforeAll
    static void setUpFilms() {
        film1 = Film.builder()
                .name("Test Film1")
                .description("Test description2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .genres(Set.of(new Genre(1, "Комедия")))
                .build();

        film2 = Film.builder()
                .name("Test Film2")
                .description("Test description2")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .mpa(new Mpa(2, "PG"))
                .genres(Set.of(new Genre(2, "Драма")))
                .build();

        film3 = Film.builder()
                .name("Test Film3")
                .description("Test description3")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .mpa(new Mpa(3, "PG-13"))
                .genres(Set.of(new Genre(6, "Боевик")))
                .build();
    }

    @Test
    void shouldReturnAllFilms() {
        filmRepository.addFilm(film1);
        filmRepository.addFilm(film2);
        filmRepository.addFilm(film3);

        assertThat(filmRepository.getFilms())
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    void shouldReturnFilmById() {
        filmRepository.addFilm(film1);

        Film film = filmRepository.getFilmById(film1.getId());

        assertThat(film)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Test Film1");
    }

    @Test
    void shouldReturnTopFilms() {
        filmRepository.addFilm(film1);
        filmRepository.addFilm(film2);
        filmRepository.addFilm(film3);

        assertThat(filmRepository.getFilms())
                .isNotEmpty()
                .hasSize(3);
    }

    @Test
    void shouldAddFilms() {
        filmRepository.addFilm(film1);
        filmRepository.addFilm(film2);
        filmRepository.addFilm(film3);

        assertThat(film2.getId())
                .isNotNull();
    }

    @Test
    void shouldUpdateFilm() {
        filmRepository.addFilm(film1);
        Film updatedFilm = Film.builder()
                .id(film1.getId())
                .name("Updated Film")
                .description("Updated description")
                .releaseDate(LocalDate.of(2022, 1, 1))
                .duration(100)
                .mpa(new Mpa(1, "G"))
                .genres(Set.of(new Genre(1, "Комедия")))
                .build();

        Film result = filmRepository.updateFilm(updatedFilm);

        assertThat(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("name", "Updated Film")
                .hasFieldOrPropertyWithValue("description", "Updated description");
    }

    @Test
    void shouldDeleteFilm() {
        filmRepository.addFilm(film1);

        filmRepository.deleteFilm(film1.getId());

        assertThrows(NotFoundException.class, () -> filmRepository.getFilmById(film1.getId()));
    }
}