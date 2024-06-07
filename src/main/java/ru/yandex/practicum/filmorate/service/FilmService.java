package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final GenreStorage genreStorage;
    private final DirectorStorage directorStorage;

    public Film getFilmById(Long filmId) {
        Collection<Genre> genres = genreStorage.findAllFilmGenre(filmId);
        Collection<Director> directors = directorStorage.findAllFilmsDirectors(filmId);
        Film film = filmStorage.getFilm(filmId).get();
        film.setGenres(genres);
        film.setDirectors(directors);
        return film;
    }

    public Collection<Film> getAllFilms() {
        Collection<Film> films = filmStorage.getAllFilms();
        for (Film film : films) {
            Collection<Genre> genres = genreStorage.findAllFilmGenre(film.getId());
            Collection<Director> directors = directorStorage.findAllFilmsDirectors(film.getId());
            film.setGenres(genres);
            film.setDirectors(directors);
        }
        return films;
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
        Collection<Film> popularFilms = filmStorage.findSomePopular(amountOfFilms);
        for (Film film : popularFilms) {
            Collection<Genre> genres = genreStorage.findAllFilmGenre(film.getId());
            Collection<Director> directors = directorStorage.findAllFilmsDirectors(film.getId());
            film.setGenres(genres);
            film.setDirectors(directors);
        }
        return popularFilms;
    }

    public Collection<Film> getDirectorsFilmsSortBy(Long id, String sortBy) {
        if (directorStorage.getDirectorById(id).isEmpty()) {
            log.warn("Режиссёра не существует");
            throw new NotFoundException("Режиссёра с таким id не существует");
        }
        if (sortBy.equals("year")) {
            List<Long> filmsIds = filmStorage.getFilmsSortByYear(id);
            List<Film> films = new ArrayList<>();
            for (Long filmId : filmsIds) {
                films.add(getFilmById(filmId));
            }
            return films;
        } else if (sortBy.equals("likes")) {
            List<Long> filmsIds = filmStorage.getFilmsSortByLikes(id);
            List<Film> films = new ArrayList<>();
            for (Long filmId : filmsIds) {
                films.add(getFilmById(filmId));
            }
            return films;
        } else {
            throw new ValidationException("Параметр 'sortBy' задан неверно");
        }
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
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Не корректная дата");
            throw new ValidationException("Фильм не мог быть выпущен в эту дату.");
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

        if (film.getDirectors() != null) {
            for (Director director : film.getDirectors()) {
                if (directorStorage.getDirectorById(director.getId()).isEmpty()) {
                    log.warn("Режиссёр не существует");
                    throw new ValidationException("Такого режиссёра не существует");
                }
            }
        }
    }
}