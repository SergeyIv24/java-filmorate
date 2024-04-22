package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
    public User getUserById(@PathVariable Long userId) {
        return userStorage.getUser(userId);
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User newUser) {
        return userStorage.addUser(newUser);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.updateUser(user);

    }

    @PutMapping("/{id}/friends/{friendId}")
    public void makeUsersFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addUserInFriends(userId, friendId);
    }



}
