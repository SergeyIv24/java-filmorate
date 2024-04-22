package ru.yandex.practicum.filmorate.storage;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();

    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public User addUser(User newUser) {
        validate(newUser);
        newUser.setId(parseId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updateUser(User user) {

        if (user.getId() == null) {
            log.warn("Не указан Id");
            throw new ValidationException("Id должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
            log.warn("Запрошен несуществующий пользователь");
            throw new ValidationException("Пользователя не существует");
        }
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    private Long parseId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(User newUser) {
        if (newUser.getEmail().isEmpty() || newUser.getEmail().isBlank()) {
            log.warn("Пользователь не указал Email");
            throw new ValidationException("Email должен быть указан");
        }

        if (!newUser.getEmail().contains("@")) {
            log.warn("Указан не верный формат Email");
            throw new ValidationException("Не верный формат Email");
        }

        if (newUser.getLogin().isEmpty() || newUser.getLogin().isBlank()) {
            log.warn("Пустой логин");
            throw new ValidationException("Логин не может быть пустым");
        }

        if (newUser.getName() == null || newUser.getName().isEmpty() || newUser.getName().isBlank()) {
            log.info("Имя не указано, имя заменено на логин");
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения");
            throw new ValidationException("Вы еще не родились(");
        }
    }

}
