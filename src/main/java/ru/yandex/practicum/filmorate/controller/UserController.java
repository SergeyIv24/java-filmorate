package ru.yandex.practicum.filmorate.controller;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {

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

        if (newUser.getName() == null ||newUser.getName().isEmpty() || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения");
            throw new ValidationException("Вы еще не родились(");
        }
        newUser.setId(parseId());
        users.put(newUser.getId(), newUser);

        return newUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {

        if (user.getId() == null) {
            log.warn("Не указан Id");
            throw new ValidationException("Id должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
            log.warn("Запрошен несуществующий пользователь");
            throw new ValidationException("Пользователя не существует");
        }

        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.warn("Не указан Email");
            throw new ValidationException("Email должен быть указан");
        }

        if (!user.getEmail().contains("@")) {
            log.warn("Указан не верный формат Email");
            throw new ValidationException("Не верный формат Email");
        }

        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.warn("Пустой логин");
            throw new ValidationException("Логин не может быть пустым");
        }

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            log.info("Имя не указано, имя заменено на логин");
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения");
            throw new ValidationException("Вы еще не родились(");
        }

        User oldUser = users.get(user.getId());
        oldUser.setName(user.getName());
        oldUser.setLogin(user.getLogin());
        oldUser.setEmail(user.getEmail());
        oldUser.setBirthday(user.getBirthday());
        users.put(user.getId(), oldUser);
        return oldUser;
    }

    private Long parseId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
