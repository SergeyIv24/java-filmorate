package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage, GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public Film getFilmById(Long filmId) {
        Collection<Genre> genres = genreStorage.findAllFilmGenre(filmId);
        Film film = filmStorage.getFilm(filmId).get();
        film.setGenres(genres);
        return film;
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film newFilm) {
        isItDuplicate(newFilm.getId());
        validate(newFilm);
        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film film) {
        isFilmExist(film.getId());
        validate(film);
        return filmStorage.updateFilm(film);
    }

    public void addLikeToFilm(Long userId, Long filmId) {
        isFilmExist(filmId);
        isUserExist(userId);
        filmStorage.addLikeToFilm(userId, filmId);
    }

    //Метод удаления лайка с фильма
    public void deleteLike(Long filmId, Long userId) {
        isFilmExist(filmId);
        isUserExist(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getSomePopularFilms(int amountOfFilms) {
        return filmStorage.findSomePopular(amountOfFilms);
    }

    private void isFilmExist(Long filmId) {
        if (filmStorage.getFilm(filmId).isEmpty()) {
            log.warn("Фильма не существует");
            throw new NotFoundException("Фильма с таким id не существует");
        }
    }

    private void isItDuplicate(Long filmId) {
        if (filmStorage.getFilm(filmId).isPresent()) {
            log.warn("Фильма уже существует");
            throw new ValidationException("Фильм уже существует");
        }
    }

    private void isUserExist(Long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь с таким id не существует");
        }
    }

    //Валидация входных данных
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

        if (film.getMpa().getId() > Constance.mpaAmount) {
            log.warn("Некорректный Mpa");
            throw new ValidationException("Такого рейтинга не существует");
        }

        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                if (genre.getId() > Constance.genresAmount) {
                    log.warn("Жанр не существует");
                    throw new ValidationException("Такого рейтинга не существует");
                }
            }
        }

    }
}
