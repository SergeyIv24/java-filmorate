package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Repository
public class FilmDbStorage extends BaseStorage<Film> implements FilmStorage {

    public FilmDbStorage(JdbcTemplate jdbcTemplate, RowMapper<Film> mapper) {
        super(jdbcTemplate, mapper);
    }

    @Override
    public Map<Long, Film> getFilms() {
        return null;
    }

    public List<Integer> getAllIntegers(String query, Object... parameters) {
        try {
            return jdbcTemplate.queryForList(query, Integer.class, parameters);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Collection<Film> getAllFilms() {
        Collection<Film> filmsWithGenres = getAllItems(SQLqueries.FIND_FILMS_WITH_GENRE); //Все фильмы у которых есть жанры
        Collection<Film> films = getAllItems(SQLqueries.GET_ALL_FILMS); //Все фильмы
        for (Film film : films) {
            if (filmsWithGenres.contains(film)) {
                film.setGenres(getAllIntegers(SQLqueries.FIND_ALL_GENRES_OF_FILM, film.getId()));
            }
        }
        return films;
    }

    @Override
    public Optional<Film> getFilm(Long filmId) {
        return getOnePosition(SQLqueries.GET_FILM_BY_ID, filmId);
    }

    @Override
    public Film addFilm(Film newFilm) {

        if (newFilm.getRating_id() == null) {
            newFilm.setRating_id(6);
        }

        Long id = insert(SQLqueries.ADD_FILM,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getRating_id());
        newFilm.setId(id);


        if (newFilm.getGenres() != null) {
            List<String> genresByFimId = new ArrayList<>();
            Long filmId = newFilm.getId();
            for (Integer genre : newFilm.getGenres()) {
                String genreAndId = String.valueOf(genre) + filmId;
                genresByFimId.add(genreAndId);
            }
            insertMany(SQLqueries.ADD_GENRE, genresByFimId);
        }
        return newFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        update(SQLqueries.UPDATE_FILM,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating_id(),
                film.getId());
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        deleteItem(SQLqueries.DELETE_FILM, film.getId());
        return film;
    }

/*    public void insertMany(Object... parameters) {
        insertMany(SQLqueries.ADD_GENRE);
    }*/
}
