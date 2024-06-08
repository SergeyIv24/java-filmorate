package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.annotation.Nonnegative;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Review {
    Long reviewId;
    @NotBlank(message = "Не заполнено содержимое отзыва")
    String content;
    @NotNull (message = "Тип отзыва не указан")
    Boolean isPositive;
    @NotNull (message = "Пользователь не указан")
    @Nonnegative
    Long userId;
    @NotNull (message = "Фильм не указан")
    Long filmId;
    Long useful;

}