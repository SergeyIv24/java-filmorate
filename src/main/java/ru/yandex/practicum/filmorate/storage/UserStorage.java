package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getAllUsers();

    Optional<User> getUser(Long userId);

    Collection<User> getFriendsUserById(Long userId);

    User addUser(User newUser);

    User updateUser(User user);

    Collection<User> findCommonFriends(Long userId, Long otherId);
}
