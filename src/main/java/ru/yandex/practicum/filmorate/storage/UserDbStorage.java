package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Map;

public class UserDbStorage implements UserStorage { //todo наследовть от базового хранилища

    @Override
    public Collection<User> getAllUsers() {
        return null;
    }

    @Override
    public User getUser(Long userId) {
        return null;
    }

    @Override
    public Collection<User> getFriendsUserById(Long userId) {
        return null;
    }

    @Override
    public User addUser(User newUser) {
        return null;
    }

    @Override
    public User updateUser(User user) {
        return null;
    }

    @Override
    public User deleteUser(User user) {
        return null;
    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }
}
