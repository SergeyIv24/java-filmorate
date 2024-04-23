package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Метод добавления пользователя в друзья
    public void addUserInFriends(Long userId, Long friendId) {
        checkExistUser(userId, friendId); //Проверка, что пользователи существуют

        if (userStorage.getUsers().get(userId).getFriends().contains(userStorage.getUsers().get(friendId))
                || userStorage.getUsers().get(friendId).getFriends().contains(userStorage.getUsers().get(userId))) {
            log.warn("Пользователь был добавлен в друзья ранее");
            throw new ValidationException("Пользователь уже есть в друзьях");
        }
        userStorage.getUsers().get(userId).getFriends().add(userStorage.getUsers().get(friendId));
        userStorage.getUsers().get(friendId).getFriends().add(userStorage.getUsers().get(userId));


    }

    //Метод удаления пользователя из друзей
    public void deleteUserFromFriends(Long userId, Long friendId) {
        checkExistUser(userId, friendId);
        userStorage.getUsers().get(userId).getFriends().remove(userStorage.getUsers().get(friendId)); //Удаление у обоих пользователей
        userStorage.getUsers().get(friendId).getFriends().remove(userStorage.getUsers().get(userId));
    }

    //Метод поиска общих друзей
    public List<User> getCommonFriends(Long userId, Long otherId) {
        checkExistUser(userId, otherId);
        User user = userStorage.getUsers().get(userId); //Текущий пользователь
        User otherUser = userStorage.getUsers().get(otherId); //Пользователь для добавления в друзья

        return user.getFriends().stream()
                .filter((user1) -> otherUser.getFriends().contains(user1)) //Если друзья User есть у otherUser - в список
                .collect(Collectors.toList());
    }

    //Проверка существует ли пользователь в мапах
    private void checkExistUser(Long userId, Long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            log.warn("Пользователя нет в мапе");
            throw new NotFoundException("Пользователь не существует");
        }

        if (!userStorage.getUsers().containsKey(friendId)) {
            log.warn("Пользователя нет в мапе");
            throw new NotFoundException("Пользователь не существует");
        }
    }


}
