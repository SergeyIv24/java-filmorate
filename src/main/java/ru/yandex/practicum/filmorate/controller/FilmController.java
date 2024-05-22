package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable Long filmId) { //Получение фильма по id
        return filmService.getFilmById(filmId);
    }

   @GetMapping()
   @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() { //Получение всех фильмов
        return filmService.getAllFilms();
    }

    @GetMapping("/popular")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms(@RequestParam int count) { //Получение популярных фильмов
        if (count != 0) {
            return filmService.getSomePopularFilms(count);
        }
        return filmService.getAllFilms();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film newFilm) { //Добавление фильма
        return filmService.addFilm(newFilm);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) { //Обновление фильма
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilm(@PathVariable(value = "id") Long id, @PathVariable(value = "userId") Long userId) { //Добавление лайка к фильму
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) { //Удаление лайка с фильма
        filmService.deleteLike(id, userId);
    }
}
