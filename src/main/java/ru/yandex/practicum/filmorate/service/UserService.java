package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDbStorage userStorage;

    public User getUserById(Long userId) {
        return userStorage.getUser(userId).get();
    }

    public Collection<User> getAllUsersById(Long id) {
        return userStorage.getFriendsUserById(id);
    }

    public Collection<User> getAllUsers() {
        Collection<User> users = userStorage.getAllUsers();
        for (User user : users) {
            Collection<User> userFriend = userStorage.getFriendsUserById(user.getId());
            user.setFriends(userFriend);
        }
        return users;
    }

    public User createUser(User newUser) {
        return userStorage.addUser(newUser);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public void deleteUser(Long userId){
        boolean isDeleted = userStorage.deleteUser(userId);
        if (!isDeleted){
            log.warn("Пользователя с таким id не существует");
            throw new NotFoundException("Пользователь не существует");
        }
        log.debug("Пользователь " + userId + " удален");
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
