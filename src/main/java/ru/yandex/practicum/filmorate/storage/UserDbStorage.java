package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserDbStorage extends BaseStorage<User> implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Override
    public Collection<User> getAllUsers() {
        return getAllItems(SQLqueries.GET_ALL_USERS);
    }

    @Override
    public Optional<User> getUser(Long userId) {
        return getOnePosition(SQLqueries.GET_USER_BY_ID, userId);
    }

    @Override
    public Collection<User> getFriendsUserById(Long userId) {
        if (getOnePosition(SQLqueries.GET_USER_BY_ID, userId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return getAllItems(SQLqueries.GET_USERS_FRIENDS, userId);
    }

    @Override
    public User addUser(User user) {
        Long id = insert(SQLqueries.ADD_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        user.setUserId(id);
        return user;
    }

    @Override
    public User updateUser(User user) {
        update(SQLqueries.UPDATE_USER, user.getLogin(), user.getName(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public User deleteUser(User user) {
        isUserExist(user.getUserId());
        deleteItem(SQLqueries.DROP_USER, user.getUserId());
        return user;
    }

    @Override
    public Collection<User> findCommonFriends(Long userId, Long otherId) {
        //Проверка наличия пользователей в базе
        if (getOnePosition(SQLqueries.GET_USER_BY_ID, userId).isEmpty()
                || getOnePosition(SQLqueries.GET_USER_BY_ID, otherId).isEmpty()) {
            throw new NotFoundException("Пользователь не найден");
        }
        return getAllItems(SQLqueries.GET_COMMON_FRIENDS, userId, otherId);
    }

    @Override
    public void makeUsersFriends() {

    }

    @Override
    public void deleteFromFriends() {

    }

    @Override
    public Map<Long, User> getUsers() {
        return null;
    }

    private void isUserExist(Long userId) {
        if (getUser(userId).isEmpty()) {
            log.warn("Запрошен несуществующий пользователь");
            throw new NotFoundException("Пользователя не существует");
        }
    }

    //todo сделать defaul в интерфейсе
    private void validate(User newUser) {
        if (newUser.getEmail().isEmpty() || newUser.getEmail().isBlank()) {
            log.warn("Пользователь не указал Email");
            throw new ValidationException("Email должен быть указан");
        }

        if (!newUser.getEmail().contains("@")) {
            log.warn("Указан не верный формат Email");
            throw new ValidationException("Не верный формат Email");
        }

        if (newUser.getLogin().isEmpty() || newUser.getLogin().isBlank()) {
            log.warn("Пустой логин");
            throw new ValidationException("Логин не может быть пустым");
        }

        if (newUser.getName() == null || newUser.getName().isEmpty() || newUser.getName().isBlank()) {
            log.info("Имя не указано, имя заменено на логин");
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Некорректная дата рождения");
            throw new ValidationException("Вы еще не родились(");
        }
    }

}
