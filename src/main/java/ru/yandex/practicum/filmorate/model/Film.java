package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Getter
@Setter
public class Film {
    private int id;

    @NotBlank(message = "Film name cannot be empty.")
    private String name;

    @Size(max = 200, message = "Description cannot exceed 200 characters.")
    private String description;

    @Past(message = "Release date must be in the past.")
    private LocalDate releaseDate;

    @Positive(message = "Duration must be a positive number.")
    private int duration;

    private Set<Integer> likes = new HashSet<>();
}
