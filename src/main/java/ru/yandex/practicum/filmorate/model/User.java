package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;

    @NotBlank(message = "Email cannot be empty.")
    @Email(message = "Email should be valid.")
    private String email;

    @NotBlank(message = "Login cannot be empty.")
    @NotBlank(message = "Login cannot contain spaces.")
    private String login;

    private String name;

    @PastOrPresent(message = "Birthday must be in the past or present.")
    private LocalDate birthday;

    private Set<Integer> friends = new HashSet<>();
}
