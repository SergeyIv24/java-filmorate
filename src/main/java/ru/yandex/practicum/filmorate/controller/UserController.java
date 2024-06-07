package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getUserById(@PathVariable Long userId) { //Получение пользователя по id
        return userService.getUserById(userId);
    }

    @GetMapping("/{id}/friends")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsersById(@PathVariable Long id) { //Получение всех друзей пользователя
        return userService.getAllUsersById(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> findCommonFriends(@PathVariable Long id, @PathVariable Long otherId) { //Поиск общих друзей
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<User> getAllUsers() { //Получение всех пользователей
        return userService.getAllUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody User newUser) { //Создание пользователя
        return userService.createUser(newUser);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@Valid @RequestBody User user) { //Обновление пользователя
        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK) //Добавление в друзья
    public void makeUsersFriends(@PathVariable(value = "id") Long id, @PathVariable(value = "friendId") Long friendId) {
        userService.addUserInFriends(id, friendId);
        userService.addUserActivity(id, friendId, Constance.FRIEND, Constance.OPERATION_ADD);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFromFriends(@PathVariable(value = "id") Long userId, @PathVariable(value = "friendId") Long friendId) { //Удаление из друзей
        userService.deleteUserFromFriends(userId, friendId);
        userService.addUserActivity(userId, friendId, Constance.FRIEND, Constance.OPERATION_REMOVE);
    }

    @GetMapping("/{id}/feed")
    @ResponseStatus(HttpStatus.OK)
    public Collection<Feed> getUserFeed(@PathVariable(value = "id") Long userId) {
        return userService.getUserFeed(userId);
    }
}
