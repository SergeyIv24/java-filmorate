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
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<Film> optionalFilm = filmStorage.getFilm(filmId);
        if (optionalFilm.isEmpty()) {
            throw new NotFoundException("Позьзователь с данным id не существует");
        }
        Film film = optionalFilm.get();
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
        Collection<Genre> uniqueGenres = checkUniqueGenres(film.getGenres());
        film.setGenres(uniqueGenres);
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Long filmId) {
        boolean isDeleted = filmStorage.deleteFilm(filmId);
        if (!isDeleted) {
            log.warn("Фильма не существует");
            throw new NotFoundException("Фильма с таким id не существует");
        }
        log.debug("Фильм " + filmId + " удален.");
    }

    public void addLikeToFilm(Long userId, Long filmId) {
        isFilmExist(filmId);
        isUserExist(userId);
        try {
            filmStorage.addLikeToFilm(userId, filmId);
        } catch (Exception e) {
            log.warn("При добавлении лайка произошла ошибка");
        }
    }

    //Метод удаления лайка с фильма
    public void deleteLike(Long filmId, Long userId) {
        isFilmExist(filmId);
        isUserExist(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Collection<Film> getSomePopularFilms(Integer amountOfFilms, Integer genreId, Integer year) {
        return filmStorage.findSomePopular(amountOfFilms, genreId, year);
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

    public Collection<Film> getCommonFilms(Long userId, Long friendId) {
        isUserExist(userId);
        isUserExist(friendId);
        return new ArrayList<>(filmStorage.getCommonFilm(userId, friendId));
    }

    public Collection<Film> getFilmByUserId(Long userId) {
        return new ArrayList<>(filmStorage.getFilmByUserId(userId));
    }

    public Collection<Film> searchFilm(String query, String by) {
        ArrayList<Film> films = new ArrayList<>();
        query = query.toLowerCase();
        if (by.equals("title")) {
            films.addAll(filmStorage.searchFilmByTitle(query));
        }
        if (by.equals("director")) {
            films.addAll(filmStorage.searchFilmByDirector(query));
        } else if (by.equals("title,director")) {
            films.addAll(filmStorage.searchFilmByBoth(query));
        }
        return films;
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

    private Collection<Genre> checkUniqueGenres(Collection<Genre> genres) {
        if (genres == null) {
            return List.of();
        }
        return genres.stream().distinct().collect(Collectors.toList());
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