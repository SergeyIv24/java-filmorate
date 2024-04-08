package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class User {
    Long id;
    @NonNull @NotBlank
    String login;
    String name;
    @Email @NonNull @NotBlank
    String email;
    @JsonFormat(pattern = "dd.MM.yyyy") //Более читаемая дата
    LocalDate birthday;
}
