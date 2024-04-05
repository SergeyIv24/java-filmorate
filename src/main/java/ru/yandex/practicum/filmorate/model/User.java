package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class User {
    Long id;
    String login;
    String name;
    String email;
    @JsonFormat(pattern = "dd.MM.yyyy")
    LocalDate birthday;
}
