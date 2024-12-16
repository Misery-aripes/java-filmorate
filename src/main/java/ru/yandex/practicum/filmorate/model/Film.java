package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Film {

    int id;

    @NotBlank(message = "The login cannot be empty and contain spaces")
    String name;

    @Size(min = 1, max = 200, message = "The maximum length of the description is 200 characters")
    String description;

    LocalDate releaseDate;

    @Positive(message = "The duration of the film should be positive")
    int duration;

    Set<Integer> usersLikes = new HashSet<>();

    public void addLike(int userId) {
        usersLikes.add(userId);
    }

    public void removeLike(int userId) {
        if (!usersLikes.contains(userId)) {
            throw new ObjectNotFoundException("A user with id = " + userId + " did not like the movie");
        }
        usersLikes.remove(userId);
    }
}