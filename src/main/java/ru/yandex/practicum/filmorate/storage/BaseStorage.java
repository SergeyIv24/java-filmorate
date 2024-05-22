package ru.yandex.practicum.filmorate.storage;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public class BaseStorage <T> extends Storage<T> {
    protected JdbcTemplate jdbcTemplate;
    protected RowMapper<T> mapper;

    public BaseStorage(JdbcTemplate jdbcTemplate, RowMapper<T> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override //todo убрать null???
    public Collection<T> getAllItems(String query) {
        try {
            return jdbcTemplate.query(query, mapper);
        } catch (EmptyResultDataAccessException ignore) {
            return null;
        }
    }

    @Override
    public Collection<T> getAllItems(String query, Object... parameters) {
        try {
            return jdbcTemplate.query(query, mapper, parameters);
        } catch (EmptyResultDataAccessException ignore) {
            return null;
        }
    }

    @Override
    public Optional<T> getOnePosition(String query, Object... parameters) {
        try {
            T result = jdbcTemplate.queryForObject(query, mapper, parameters);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteItem(String query, Object... parameters) {
        return jdbcTemplate.update(query, parameters) > 0;
    }

    @Override
    public Long insert(String query, Object... parameters) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    for (int idx = 0; idx < parameters.length; idx++) {
                        ps.setObject(idx + 1, parameters[idx]);
                    }
                    return ps;},
                keyHolder);

        Long id = keyHolder.getKeyAs(Long.class);

        if (id != null) {
            return id;
        } else {
            throw new ValidationException("Не удалось сохранить данные");
        }
    }

    public void insertMany(String query, Integer genreId, Long filmId) {
        jdbcTemplate.update(query, genreId, filmId);
    }

    public void insertMany(String query, List<String> parameters) {
        for (String parameter : parameters) {
            Integer filmId = Integer.parseInt(parameter.substring(1));
            Integer genre = Integer.parseInt(parameter.substring(0, 1));
            jdbcTemplate.update(query, genre, filmId);
        }
    }

    @Override
    public void update(String query, Object... parameters) {
        int rowsUpdated = jdbcTemplate.update(query, parameters);
        if (rowsUpdated == 0) {
            throw new ValidationException("Не удалось обновить данные");
        }
    }
}