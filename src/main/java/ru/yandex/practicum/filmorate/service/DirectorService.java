package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DirectorService {

    private static final Logger log = LoggerFactory.getLogger(DirectorService.class);
    private final DirectorStorage directorStorage;

    public Collection<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirectorById(Long id) {
        return directorStorage.getDirectorById(id)
                .orElseThrow(() -> new NotFoundException("Режиссёра с таким id не существует"));
    }

    public Director addDirector(Director director) {
        isItDuplicate(director.getId());
        return directorStorage.addDirector(director);
    }

    public Director updateDirector(Director newDirector) {
        isDirectorExist(newDirector.getId());
        return directorStorage.updateDirector(newDirector);
    }

    public void deleteDirector(Long id) {
        directorStorage.deleteDirector(id);
    }

    private void isItDuplicate(Long id) {
        if (directorStorage.getDirectorById(id).isPresent()) {
            log.warn("Режиссёр уже существует");
            throw new ValidationException("Режиссёр уже существует");
        }
    }

    private void isDirectorExist(Long id) {
        if (directorStorage.getDirectorById(id).isEmpty()) {
            log.warn("Режиссёра не существует");
            throw new NotFoundException("Режиссёра с таким id не существует");
        }
    }
}