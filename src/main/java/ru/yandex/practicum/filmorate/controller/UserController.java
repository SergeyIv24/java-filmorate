package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }


    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long userId) { //Получение пользователя по id
        return userStorage.getUser(userId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsersById(@PathVariable Long id) { //Получение всех друзей пользователя
        return userStorage.getFriendsUserById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) { //Поиск общих друзей
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() { //Получение всех пользователей
        return userStorage.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User newUser) { //Создание пользователя
        return userStorage.addUser(newUser);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) { //Обновление пользователя
        return userStorage.updateUser(user);

    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK) //Добавление в друзья
    public void makeUsersFriends(@PathVariable(value = "id") Long id, @PathVariable(value = "friendId") Long friendId) {
        userService.addUserInFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromFriends(@PathVariable(value = "id") Long userId, @PathVariable(value = "friendId") Long friendId) { //Удаление из друзей
        userService.deleteUserFromFriends(userId, friendId);
    }

}
