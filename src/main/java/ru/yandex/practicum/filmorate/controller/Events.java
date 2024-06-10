package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
public enum Events {
    LIKE(1),
    REVIEW(2),
    FRIEND(3);

    private final int event;

    Events(int event) {
        this.event = event;
    }
}
