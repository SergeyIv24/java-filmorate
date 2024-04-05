package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {

        if (newUser.getEmail().isEmpty() || newUser.getEmail().isBlank()) {
            throw new ValidationException("Email должен быть указан");
        }

        if (!newUser.getEmail().contains("@")) {
            throw new ValidationException("Не верный формат Email");
        }

        if (newUser.getLogin().isEmpty() || newUser.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }

        if (newUser.getName().isEmpty() || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Вы еще не родились(");
        }
        newUser.setId(parseId());
        users.put(newUser.getId(), newUser);

        return newUser;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {

        if (user.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователя не существует");
        }

        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            throw new ValidationException("Email должен быть указан");
        }

        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Не верный формат Email");
        }

        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            throw new ValidationException("Логин не может быть пустым");
        }

        if (user.getName().isEmpty() || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
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
