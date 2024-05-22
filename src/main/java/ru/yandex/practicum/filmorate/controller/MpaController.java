package ru.yandex.practicum.filmorate.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.Collection;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaController {

    private final MpaService mpaService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<Mpa> getAllMpa() {
        return mpaService.getAllMpa();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mpa getMpaById(@PathVariable Integer id) {
        return mpaService.getMpaById(id);
    }


}
