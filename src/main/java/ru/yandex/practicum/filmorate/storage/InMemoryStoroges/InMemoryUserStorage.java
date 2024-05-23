package ru.yandex.practicum.filmorate.storage.InMemoryStoroges;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
@Qualifier
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    Map<Long, User> users = new HashMap<>(); //Мапа для хранения пользователей

    public Map<Long, User> getUsers() {
        return users;
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUser(Long userId) {
        isUserExist(userId);
        return Optional.of(users.get(userId));
    }

    public Collection<User> getFriendsUserById(Long userId) {
        isUserExist(userId);
        return users.get(userId).getFriends();
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
        isUserExist(user.getId());
        validate(user);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Collection<User> findCommonFriends(Long userId, Long otherId) {
        return null;
    }

    private Long parseId() {
        long currentMaxId = users.keySet().stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void isUserExist(Long userId) {
        if (!users.containsKey(userId)) {
            log.warn("Запрошен несуществующий пользователь");
            throw new NotFoundException("Пользователя не существует");
        }
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
