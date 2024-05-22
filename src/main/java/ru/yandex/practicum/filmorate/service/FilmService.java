package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

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

        return filmStorage.addFilm(newFilm);
    }

    public Film updateFilm(Film film) {
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
}
