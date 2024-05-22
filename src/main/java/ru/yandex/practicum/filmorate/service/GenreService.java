package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.Constance;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Genre getGenreById(Integer genreId) {
        validateGenres(genreId);
        return genreStorage.getGenreById(genreId).get();
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    private void validateGenres(Integer genreId) {
        if (genreId > Constance.genresAmount) {
            throw new NotFoundException("Такого жанра не существует");
        }
    }
}
