package ru.yandex.practicum.filmorate.model;

import jdk.jfr.BooleanFlag;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Review {
    Long reviewId;
    @NotBlank(message = "Не заполнено содержимое отзыва")
    String content;
    @NotNull
    @BooleanFlag
    boolean isPositive;
    @NotNull (message = "Пользователь не указан")
    Long userId;
    @NotNull (message = "Фильм не указан")
    Long filmsId;
    Long useful;
}