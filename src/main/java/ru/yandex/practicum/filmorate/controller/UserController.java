package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAllUsers() {
        return null;
    }

    @PostMapping
    public User createUser(@RequestBody User newUser) {
        return null;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return null;
    }

}
