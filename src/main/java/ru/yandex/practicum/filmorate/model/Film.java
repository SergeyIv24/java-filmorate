package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    Long id;
    @NotBlank @NotNull
    String name;
    String description;
    @JsonFormat(pattern = "dd.MM.yyyy") //Более читаемая дата
    LocalDate releaseDate;
    Duration duration;

    //Методы для обработки продолжительности в запросах, переданных, как количество минут
    @JsonGetter("duration")
    public long getDurationInMinutes() {
        return duration.toMinutes();
    }

    @JsonSetter("duration")
    public void DurationInMinutes(long duration) {
        this.duration= Duration.ofMinutes(duration);
    }
}
