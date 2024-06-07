package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Repository
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

    public Collection<Film> findSomePopular(Integer amount, Integer genreId, Integer year) {
        StringBuilder sql = new StringBuilder(SQLqueries.GET_POPULAR);
        if (genreId != null && year != null) {
            sql.append(" INNER JOIN films_geners fg ON fg.films_id = f.films_id ")
                    .append(" WHERE fg.gener_id = ? AND EXTRACT(YEAR FROM f.release_date) = ? ")
                    .append(" GROUP BY f.films_id, f.name, f.description, f.release_date, f.duration, f.rating_id, rat.name ")
                    .append(" ORDER BY pop.popular DESC ");
        } else if (genreId != null) {
            sql.append(" INNER JOIN films_geners fg ON fg.films_id = f.films_id ")
                    .append(" WHERE fg.gener_id = ? ")
                    .append(" GROUP BY f.films_id, f.name, f.description, f.release_date, f.duration, f.rating_id, rat.name ")
                    .append(" ORDER BY pop.popular DESC ");
        } else if (year != null) {
            sql.append(" WHERE EXTRACT(YEAR FROM f.release_date) = ? ")
                    .append(" GROUP BY f.films_id, f.name, f.description, f.release_date, f.duration, f.rating_id, rat.name ")
                    .append(" ORDER BY pop.popular DESC ");
        } else {
            sql.append(" GROUP BY f.films_id, f.name, f.description, f.release_date, f.duration, f.rating_id, rat.name ")
                    .append(" ORDER BY pop.popular DESC ");
        }

        List<Object> params = new ArrayList<>();
        params.add(amount);
        if (genreId != null) {
            params.add(genreId);
        }
        if (year != null) {
            params.add(year);
        }

        return getAllItems(sql.toString(), params.toArray());
    }
}