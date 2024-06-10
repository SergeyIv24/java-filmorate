package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;

@Getter
public enum Operations {
    OPERATION_REMOVE(1),
    OPERATION_ADD(2),
    OPERATION_UPDATE(3);

    private final int operation;

    Operations(int operation) {
        this.operation = operation;
    }
}
