package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.Collection;
import java.util.Optional;

@Repository
public class DirectorStorage extends BaseStorage<Director> {
    public DirectorStorage(JdbcTemplate jdbcTemplate, RowMapper<Director> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Collection<Director> getAllDirectors() {
        return getAllItems(SQLqueries.GET_ALL_DIRECTORS);
    }

    public Optional<Director> getDirectorById(Long id) {
        return getOnePosition(SQLqueries.GET_DIRECTOR_BY_ID, id);
    }

    public Director addDirector(Director director) {
        Long id = insert(SQLqueries.ADD_DIRECTOR, director.getName());
        director.setId(id);
        return director;
    }

    public Director updateDirector(Director newDirector) {
        update(SQLqueries.UPDATE_DIRECTOR,
                newDirector.getName(),
                newDirector.getId());
        return newDirector;
    }

    public void deleteDirector(Long id) {
        deleteItem(SQLqueries.DELETE_DIRECTOR, id);
    }

    public Collection<Director> findAllFilmsDirectors(Long filmId) {
        return getAllItems(SQLqueries.FIND_ALL_DIRECTORS_OF_FILM, filmId);
    }
}