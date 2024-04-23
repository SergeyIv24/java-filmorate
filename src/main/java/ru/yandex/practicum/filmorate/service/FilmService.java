package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
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
            //todo log
            throw new NotFoundException("Фильма с таким id не существует");
        }
    }

    private void isUserExist(Long userId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            //todo log
            throw new NotFoundException("Пользователь с таким id не существует");
        }
    }
}
