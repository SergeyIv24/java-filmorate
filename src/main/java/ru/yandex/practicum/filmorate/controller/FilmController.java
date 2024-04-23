package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) { // Контроллер зависит от хранилища фильмов - внедрение через констр
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping("/{filmId}")
    @ResponseStatus(HttpStatus.OK)
    public Film getFilmById(@PathVariable Long filmId) { //Получение фильма по id
        return filmStorage.getFilm(filmId);
    }

   @GetMapping()
   @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms() { //Получение всех фильмов
        return filmStorage.getAllFilms();
    }

    @GetMapping("/popular/{count}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Film> getAllFilms(@PathVariable(required = false) int count) { //Получение популярных фильмов
        if (count != 0) {
            return filmService.getSomePopularFilms(count);
        }
        return filmStorage.getAllFilms();
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Film addFilm(@Valid @RequestBody Film newFilm) { //Добавление фильма
        return filmStorage.addFilm(newFilm);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Film updateFilm(@Valid @RequestBody Film film) { //Обновление фильма
        return filmStorage.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void addLikeToFilm(@PathVariable Long id, @PathVariable Long userId) { //Добавление лайка к фильму
        filmService.addLikeToFilm(userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) { //Удаление лайка с фильма
        filmService.deleteLike(id, userId);
    }
}
