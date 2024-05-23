package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    UserDbStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(Long userId) {
        return userStorage.getUser(userId).get();
    }

    public Collection<User> getAllUsersById(Long id) {
        return userStorage.getFriendsUserById(id);
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User newUser) {
        return userStorage.addUser(newUser);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    //Метод добавления пользователя в друзья
    public void addUserInFriends(Long userId, Long friendId) {
        userStorage.makeUsersFriends(userId, friendId);
    }

    //Метод удаления пользователя из друзей
    public void deleteUserFromFriends(Long userId, Long friendId) {
        userStorage.deleteFromFriends(userId, friendId);
    }

    //Метод поиска общих друзей
    public Collection<User> getCommonFriends(Long userId, Long otherId) {
         return userStorage.findCommonFriends(userId, otherId);
    }
}
