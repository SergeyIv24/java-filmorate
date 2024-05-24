package ru.yandex.practicum.filmorate.service;


import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Constance;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class GenreService {
    private static final Logger log = LoggerFactory.getLogger(GenreService.class);
    private final GenreStorage genreStorage;

    public Genre getGenreById(Integer genreId) {
        validateGenres(genreId);
        return genreStorage.getGenreById(genreId).get();
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    private void validateGenres(Integer genreId) {
        if (genreId > Constance.genresAmount) {
            log.warn("Жанр не существует");
            throw new NotFoundException("Такого жанра не существует");
        }
    }
}
