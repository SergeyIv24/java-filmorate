package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film newFilm) {
                validate(newFilm);
        newFilm.setId(parseId());
        films.put(newFilm.getId(), newFilm);

        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() == null) {
            log.warn("Не заполнено Id");
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            log.warn("Запрошен несуществующий фильм");
            throw new ValidationException("Фильм не существует");
        }

        validate(film);

        films.put(film.getId(), film);
        return film;
    }

    private Long parseId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            log.warn("Не заполнено название фильма");
            throw new ValidationException("Название фильма должно быть заполнено");
        }

        if (film.getDescription().length() > 200) {
            log.warn("Описание менее 200 символов");
            throw new ValidationException("Описание должно быть менее 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Не корректная дата");
            throw new ValidationException("Фильм не мог быть выпущен в эту дату.");
        }

        if (film.getDuration().toMinutes() <= 0) {
            log.warn("Некорректная продолжительность");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
