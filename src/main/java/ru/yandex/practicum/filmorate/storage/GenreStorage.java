package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreStorage extends BaseStorage<Genre> {
    public GenreStorage(JdbcTemplate jdbcTemplate, RowMapper<Genre> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Optional<Genre> getGenreById(Integer genreId) {
        return getOnePosition(SQLqueries.GET_GENRE_BY_ID, genreId);
    }

    public Collection<Genre> getAllGenres() {
        return getAllItems(SQLqueries.GET_ALL_GENRES);
    }
}
