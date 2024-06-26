package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.Constance;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class MpaService {
    private static final Logger log = LoggerFactory.getLogger(MpaService.class);
    private final MpaStorage mpaStorage;

    public Collection<Mpa> getAllMpa() {
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(Integer id) {
        validateMpa(id);
        return mpaStorage.getMpaById(id).get();
    }

    private void validateMpa(Integer mpaId) {
        if (mpaId > Constance.mpaAmount) {
            log.warn("Некорректный Mpa");
            throw new NotFoundException("Такого рейтинга не существует");
        }
    }
}
