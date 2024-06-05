package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Collection;

@Data
//@JsonIgnoreProperties("friends")
public class User {
    Long id;
    @NotBlank
    String login;
    String name;
    @Email @NotBlank
    String email;
    LocalDate birthday;
    @EqualsAndHashCode.Exclude
    Collection<User> friends;
}
