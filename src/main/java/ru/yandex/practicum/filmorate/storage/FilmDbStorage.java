package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
@Slf4j
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
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
        setDirectors(newFilm, id);
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
        if (film.getDirectors() != null) {
            jdbcTemplate.update(SQLqueries.DELETE_OLD_DIRECTORS, film.getId());
        }
        if (film.getGenres() != null) {
            jdbcTemplate.update(SQLqueries.DELETE_OLD_GENERS, film.getId());
        }
        setGenres(film, film.getId());
        setDirectors(film, film.getId());
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

    private void setDirectors(Film newFilm, Long id) {
        if (newFilm.getDirectors() != null) {
            Set<Director> uniqueDirectors = Set.copyOf(newFilm.getDirectors());
            for (Director director : uniqueDirectors) {
                insertMany(SQLqueries.ADD_DIRECTORS_TO_FILM, director.getId(), id);
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

    public List<Long> getFilmsSortByYear(Long id) {
        List<Long> filmIds = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(SQLqueries.GET_DIRECTOR_FILMS_SORT_BY_YEAR, id);
        while (rows.next()) {
            filmIds.add(rows.getLong("films_id"));
        }
        return filmIds;
    }

    public List<Long> getFilmsSortByLikes(Long id) {
        List<Long> filmIds = new ArrayList<>();
        SqlRowSet rows = jdbcTemplate.queryForRowSet(SQLqueries.GET_DIRECTOR_FILMS_SORT_BY_LIKES, id);
        while (rows.next()) {
            filmIds.add(rows.getLong("films_id"));
        }
        return filmIds;
    }
}