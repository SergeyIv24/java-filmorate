package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

public class UserDbService {
    private static final Logger log = LoggerFactory.getLogger(UserDbService.class);
    UserDbStorage userStorage;

    @Autowired
    public UserDbService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(Long userId) {
        return userStorage.getUser(userId).get();
    }


}
