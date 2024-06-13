package ru.yandex.practicum.filmorate.storage.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class FilmMapper implements RowMapper<Film> {
    private final DirectorStorage directorStorage;
    private final GenreStorage genreStorage;


    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(rs.getLong("FILMS_ID"));
        film.setName(rs.getString("NAME"));
        film.setDescription(rs.getString("DESCRIPTION"));
        LocalDate realese = rs.getTimestamp("RELEASE_DATE").toLocalDateTime().toLocalDate();
        film.setReleaseDate(realese);
        Duration duration = Duration.ofMinutes(rs.getInt("DURATION"));
        film.setDuration(duration);
        Integer mpaId = rs.getInt("RATING_ID");
        String mpaName = rs.getString("Mpa_name");
        Mpa mpa = new Mpa();
        mpa.setId(mpaId);
        mpa.setName(mpaName);
        film.setMpa(mpa);
        film.setDirectors(directorStorage.findAllFilmsDirectors(film.getId()));
        film.setGenres(genreStorage.findAllFilmGenre(film.getId()));
        return film;
    }
}
