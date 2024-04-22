package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
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

    public boolean addLikeToFilm(Long userId, Long filmId) {
        if (!isFilmExist(filmId)) { //Если фильма нет
            return false;
        }

        if (!isUserExist(userId)) { //Если нет пользователя
            return false;
        }
        return filmStorage.getFilms().get(filmId).getUsersWhoLiked().add(userId);
    }

    //Метод удаления лайка с фильма
    public boolean deleteLike(Long filmId, Long userId) {
        if (!isFilmExist(filmId)) { //Если фильма нет
            return false;
        }

        if (!isUserExist(userId)) { //Если нет пользователя
            return false;
        }
        return filmStorage.getFilms().get(filmId).getUsersWhoLiked().remove(userId);
    }

    public List<Film> get10PopularFilms() {
        return filmStorage.getFilms().values().stream().sorted((film1, film2) -> { //Сравнение по количеству лайков (убыванию)
            Integer countOfLikes1 = film1.getUsersWhoLiked().size();
            Integer countOfLikes2 = film2.getUsersWhoLiked().size();
            return countOfLikes2.compareTo(countOfLikes1);
        }).limit(10).collect(Collectors.toList());
    }





    private boolean isFilmExist(Long filmId) {
        if (filmStorage.getFilms().containsKey(filmId)) {
            return true;
        }
        throw new ValidationException("Фильма с таким id не существует");
    }

    private boolean isUserExist(Long userId) {
        if (userStorage.getUsers().containsKey(userId)) {
            return true;
        }
        throw new ValidationException("Пользователь с таким id не существует");
    }

}