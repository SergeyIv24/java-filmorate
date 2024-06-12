package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

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
    public Collection<Film> getAllPopularFilms(@RequestParam(required = false, defaultValue = "10") Integer count,
                                               @RequestParam(required = false) Integer genreId,
                                               @RequestParam(required = false) Integer year) { //Получение популярных фильмов
        return filmService.getSomePopularFilms(count, genreId, year);
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFilm(@PathVariable(value = "id") Long id) {
        filmService.deleteFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilm(@PathVariable(value = "id") Long id, @PathVariable(value = "userId") Long userId) { //Добавление лайка к фильму
        userService.addUserActivity(userId, id, Events.LIKE, Operations.OPERATION_ADD);
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) { //Удаление лайка с фильма
        filmService.deleteLike(id, userId);
        userService.addUserActivity(userId, id, Events.LIKE, Operations.OPERATION_REMOVE);
    }

    @GetMapping("director/{directorId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getDirectorsFilmsSortBy(@PathVariable Long directorId,
                                                    @RequestParam(required = false, defaultValue = "year") String sortBy) {
        return filmService.getDirectorsFilmsSortBy(directorId, sortBy);
    }

    @GetMapping("/common")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getCommonFilms(@RequestParam("userId") Long userId, @RequestParam("friendId") Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }
}
