package ru.yandex.practicum.filmorate.storage;

import java.util.Collection;
import java.util.Optional;

abstract class Storage<T> {
    abstract Collection<T> getAllItems(String query);

    abstract Collection<T> getAllItems(String query, Object... parameters);

    abstract Optional<T> getOnePosition(String query, Object... parameters);

    abstract boolean deleteItem(String query, Object... parameters);

    abstract Long insert(String query, Object... parameters);

    abstract void insertMany(String query, Long id, Long anotherId);

    abstract void update(String query, Object... parameters);
}