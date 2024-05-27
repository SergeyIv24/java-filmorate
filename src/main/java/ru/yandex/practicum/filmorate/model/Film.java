package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.*;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    Long id;
    @NotBlank(message = "Не заполнено название фильма")
    String name;
    @NotBlank(message = "Описание менее 200 символов")
    @Size(min = 1, max = 200)
    String description;
    @PastOrPresent(message = "Дата выхода не может быть в будущем")
    LocalDate releaseDate;
    @DurationMin
    Duration duration;
    @NotNull(message = "Такого рейтинга не существует")
    Mpa mpa;
    @EqualsAndHashCode.Exclude
    Collection<Genre> genres;

    @EqualsAndHashCode.Exclude
    @JsonIgnore
    Set<Long> usersWhoLiked = new HashSet<>(); //Хранятся только id пользователь, кто поставил лайк

    //Методы для обработки продолжительности в запросах, переданных, как количество минут
    @JsonGetter("duration")
    public long getDurationInMinutes() {
        return duration.toMinutes();
    }

    @JsonSetter("duration")
    public void durationInMinutes(long duration) {
        this.duration = Duration.ofMinutes(duration);
    }


}
