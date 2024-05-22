package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.Optional;

@Repository
public class MpaStorage extends BaseStorage<Mpa> {
    public MpaStorage(JdbcTemplate jdbcTemplate, RowMapper<Mpa> mapper) {
        super(jdbcTemplate, mapper);
    }

    public Collection<Mpa> getAllMpa() {
        return getAllItems(SQLqueries.GET_ALL_MPA);
    }

    public Optional<Mpa> getMpaById(Integer id) {
        return getOnePosition(SQLqueries.GET_MPA_BY_ID, id);
    }
}
