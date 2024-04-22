package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {

    Map<Long, Film> getFilms();

    Collection<Film> getAllFilms();

    Film getFilm(Long filmId);

    Film addFilm(Film newFilm);

    Film updateFilm(Film film);

    Film deleteFilm(Film film);
}
