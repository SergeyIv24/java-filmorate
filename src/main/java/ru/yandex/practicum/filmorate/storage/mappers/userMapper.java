package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class userMapper implements RowMapper<User> {
    //Маппер для извлечения пользователей из БД
    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(resultSet.getLong("id"));
        user.setName(resultSet.getString("login"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        LocalDate birthDay = resultSet.getTimestamp("birth_day").toLocalDateTime().toLocalDate();
        user.setBirthday(birthDay);
        return user;
    }
}
