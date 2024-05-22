package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return getAllItems(SQLqueries.GET_ALL_FILMS); //Все фильмы
    }

    @Override
    public Optional<Film> getFilm(Long filmId) {
        return getOnePosition(SQLqueries.GET_FILM_BY_ID, filmId);
    }

    @Override
    public Film addFilm(Film newFilm) {
        Long id = insert(SQLqueries.ADD_FILM,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration().toMinutes(),
                newFilm.getMpa().getId());
        newFilm.setId(id);
        setGenres(newFilm, id);
        return newFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        update(SQLqueries.UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        //setGenres(film, film.getId());
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        //deleteItem(SQLqueries.DELETE_FILM, film.getId());
        return film;
    }

    private void setGenres(Film newFilm, Long id) {
        if (newFilm.getGenres() != null) {
            Set<Genre> uniqueGenres = Set.copyOf(newFilm.getGenres());
            for (Genre genre : uniqueGenres) {
                insertMany(SQLqueries.ADD_GENRE, Long.valueOf(genre.getId()), id);
            }
        }
    }



    public void addLikeToFilm(Long userId, Long filmId) {
        insertMany(SQLqueries.ADD_LIKE_TO_FILM, filmId, userId);
    }

    public void deleteLike(Long filmId, Long userId) {
        deleteItem(SQLqueries.DELETE_LIKE, filmId, userId);
    }

    public Collection<Film> findSomePopular(Integer amount) {
        return getAllItems(SQLqueries.GET_POPULAR, amount);
    }
}
