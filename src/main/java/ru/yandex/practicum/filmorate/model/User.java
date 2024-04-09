package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Data
public class User {
    Long id;
    @NotBlank
    String login;
    String name;
    @Email @NotBlank
    String email;
    //@JsonFormat(pattern = "dd.MM.yyyy") //Более читаемая дата, коммент так как не проходят тесты
    LocalDate birthday;
}
