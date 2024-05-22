package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@JsonIgnoreProperties("friends")
public class User {
    Long id;
    @NotBlank
    String login;
    String name;
    @Email @NotBlank
    String email;
    LocalDate birthday;

    @Deprecated
    @EqualsAndHashCode.Exclude
    Set<User> friends = new HashSet<>();
}
