package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAllFilms() {
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film newFilm) {

        if (newFilm.getName().isEmpty() || newFilm.getName().isBlank()) {
            throw new ValidationException("Название фильма должно быть заполнено");
        }

        if (newFilm.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть менее 200 символов");
        }

        if (newFilm.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не мог быть выпущен в эту дату.");
        }

        if (newFilm.getDuration().toMinutes() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        newFilm.setId(parseId());
        films.put(newFilm.getId(), newFilm);

        return newFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (film.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм не существует");
        }

        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("Название фильма должно быть заполнено");
        }

        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание должно быть менее 200 символов");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не мог быть выпущен в эту дату.");
        }

        if (film.getDuration().toMinutes() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }

        Film oldFilm = films.get(film.getId());
        oldFilm.setDescription(film.getDescription());
        oldFilm.setDuration(film.getDuration());
        oldFilm.setReleaseDate(film.getReleaseDate());
        oldFilm.setName(film.getName());
        films.put(oldFilm.getId(), oldFilm);
        return film;
    }

    private Long parseId() {
        long currentMaxId = films.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
