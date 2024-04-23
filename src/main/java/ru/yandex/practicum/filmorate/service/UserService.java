package ru.yandex.practicum.filmorate.service;

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

    UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    //Метод добавления пользователя в друзья
    public boolean addUserInFriends(Long userId, Long friendId) {
        if (!checkExistUser(userId, friendId)) {
            return false;
        }

        User user = userStorage.getUsers().get(userId); //Текущий пользователь
        User userForAddingToFriends = userStorage.getUsers().get(friendId); //Пользователь для добавления в друзья

        if (user.getFriends().add(userForAddingToFriends)  //Добавление обоим пользователям друг друга
                && userForAddingToFriends.getFriends().add(user)) { //True - успех, false - не добавлен
            return true;
        }
        //todo log
        throw new ValidationException("Пользователь уже есть в друзьях");
    }

    //Метод удаления пользователя из друзей
    public boolean deleteUserFromFriends(Long userId, Long friendId) {
        if (!checkExistUser(userId, friendId)) {
            return false;
        }
        User user = userStorage.getUsers().get(userId); //Текущий пользователь
        User userForDeletingFromFriends = userStorage.getUsers().get(friendId); //Пользователь для добавления в друзья

        return user.getFriends().remove(userForDeletingFromFriends) //Удаление у обоих пользователей
                && userForDeletingFromFriends.getFriends().remove(user);
    }

    //Метод поиска общих друзей
    public List<User> getCommonFriends(Long userId, Long otherId) {
        if (!checkExistUser(userId, otherId)) {
            return null;
        }

        User user = userStorage.getUsers().get(userId); //Текущий пользователь
        User otherUser = userStorage.getUsers().get(otherId); //Пользователь для добавления в друзья

        return user.getFriends().stream()
                .filter((user1) -> otherUser.getFriends().contains(user1)) //Если друзья User есть у otherUser - в список
                .collect(Collectors.toList());
    }

    //Проверка существует ли пользователь
    private boolean checkExistUser(Long userId, Long friendId) {
        if (!userStorage.getUsers().containsKey(userId)) {
            //todo log
            throw new NotFoundException("Пользователь не существует");
        }

        if (!userStorage.getUsers().containsKey(friendId)) {
            //todo log
            throw new NotFoundException("Пользователь не существует");
        }
        return true;
    }


}
