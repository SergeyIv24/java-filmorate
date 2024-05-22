package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    Long id;
    @NotBlank
    String name;
    String description;
    //@JsonFormat(pattern = "dd.MM.yyyy") //Более читаемая дата, коммент так как не проходят тесты
    LocalDate releaseDate;
    Duration duration;
    Integer rating_id;
    @EqualsAndHashCode.Exclude
    List<Integer> genres = new ArrayList<>();
    @EqualsAndHashCode.Exclude
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
