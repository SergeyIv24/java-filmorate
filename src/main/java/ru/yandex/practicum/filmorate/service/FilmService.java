package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Constance;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private static final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Autowired
    public FilmService(FilmDbStorage filmStorage, UserDbStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilm(filmId).get();
    }

    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film newFilm) {
        validate(newFilm);
        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film film) {
        validate(film);
        return filmStorage.updateFilm(film);
    }

    public void addLikeToFilm(Long userId, Long filmId) {
        isFilmExist(filmId);
        isUserExist(userId);
        filmStorage.getFilms().get(filmId).getUsersWhoLiked().add(userId);
    }

    //Метод удаления лайка с фильма
    public void deleteLike(Long filmId, Long userId) {
        isFilmExist(filmId);
        isUserExist(userId);
        filmStorage.getFilms().get(filmId).getUsersWhoLiked().remove(userId);
    }

    public List<Film> getSomePopularFilms(int amountOfFilms) {
        if (amountOfFilms == 0) {
            amountOfFilms = 10;
        }

        return filmStorage.getFilms().values().stream().sorted((film1, film2) -> { //Сравнение по количеству лайков (убыванию)
            Integer countOfLikes1 = film1.getUsersWhoLiked().size();
            Integer countOfLikes2 = film2.getUsersWhoLiked().size();
            return countOfLikes2.compareTo(countOfLikes1);
        }).limit(amountOfFilms).collect(Collectors.toList());
    }

    private void isFilmExist(Long filmId) {
        if (!filmStorage.getFilms().containsKey(filmId)) {
            log.warn("Фильма нет в мапе");
            throw new NotFoundException("Фильма с таким id не существует");
        }
    }

    private void isUserExist(Long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            log.warn("Пользователя нет в мапе");
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
