package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.Events;
import ru.yandex.practicum.filmorate.controller.Operations;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FeedDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserDbStorage userStorage;
    private final FeedDbStorage feedStorage;
    private final FilmService filmService;

    public User getUserById(Long userId) {
        Optional<User> userOptional = userStorage.getUser(userId);
        if (userOptional.isEmpty()) {
            log.warn("Пользователь с id " + userId + " не найден");
            throw new NotFoundException("Пользователь с данным id не существует");
        }
        return userOptional.get();
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

    public void deleteUser(Long userId) {
        boolean isDeleted = userStorage.deleteUser(userId);
        if (!isDeleted) {
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

    public Collection<Feed> getUserFeed(Long userId) {
        if (userStorage.getUser(userId).isEmpty()) {
            log.warn("Пользователь не существует");
            throw new NotFoundException("Пользователь не существует");
        }
        return feedStorage.getUsersFeed(userId);
    }

    public void addUserActivity(Long userId, Long entityId, Events event, Operations operation) {
        feedStorage.addUserActivity(userId, entityId, event, operation);
    }

    public Collection<Film> getRecommendations(Long userId) {
        getUserById(userId);
        ArrayList<User> allUser = new ArrayList<>(getAllUsers());
        ArrayList<Film> maxCommonFilm = null;
        int countMaxCommonFilm = 0;
        Long userMaxCommonId = -1L;
        for (User userTwo : allUser) {
            ArrayList<Film> commonFilms = new ArrayList<>(filmService.getCommonFilms(userId, userTwo.getId()));
            if (commonFilms.size() > countMaxCommonFilm && userTwo.getId() != userId) {
                countMaxCommonFilm = commonFilms.size();
                maxCommonFilm = commonFilms;
                userMaxCommonId = userTwo.getId();
            }
        }
        if (countMaxCommonFilm == 0) {
            return new ArrayList<>();
        } else {
            ArrayList<Film> userMaxCommonFilms = new ArrayList<>(filmService.getFilmByUserId(userMaxCommonId));
            userMaxCommonFilms.removeAll(maxCommonFilm);
            return userMaxCommonFilms;
        }
    }
}
