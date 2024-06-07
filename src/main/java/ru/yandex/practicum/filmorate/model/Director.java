package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Director {
    Long id;
    @NotBlank(message = "Не заполнено имя режиссёра")
    String name;
}