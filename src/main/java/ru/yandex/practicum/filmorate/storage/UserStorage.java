package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {

/*    Map<Long, User> users = new HashMap<>();

    default Map<Long, User> getUsers() {
        return users;
    }*/
    Collection<User> getAllUsers();

    User getUser(Long userId);
    Collection<User> getFriendsUserById(Long userId);

    User addUser(User newUser);

    User updateUser(User user);

    User deleteUser(User user);

    Map<Long, User> getUsers();
}
