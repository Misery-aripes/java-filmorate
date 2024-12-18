package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    int id;

    @Email(message = "There is an error in the email address")
    String email;

    @NotBlank(message = "The login cannot be empty and contain spaces")
    String login;

    String name;

    @PastOrPresent(message = "The date of birth cannot be in the future")
    LocalDate birthday;

    Set<Integer> friends = new HashSet<>();

    public void addFriend(int friendId) {
        friends.add(friendId);
    }

    public void removeFriend(int friendId) {
        friends.remove(friendId);
    }
}