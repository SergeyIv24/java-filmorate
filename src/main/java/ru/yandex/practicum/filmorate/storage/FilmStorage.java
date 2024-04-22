package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> films = new HashMap<>(); //Фильмы в мапе

    default Map<Long, Film> getFilms() {
        return films;
    }

    Collection<Film> getAllFilms();

    Film addFilm(Film newFilm);

    Film updateFilm(Film film);

    Film deleteFilm(Film film);
}
