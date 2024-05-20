package ru.yandex.practicum.filmorate.storage;

public class SQLqueries {
    public static final String GET_ALL_USERS = "SELECT * FROM users;";
    public static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    public static final String DROP_USER = "DELETE FROM users WHERE id = ?;";
    public static final String ADD_USER = "INSERT INTO users(login, name, email, birth_day)" +
            "VALUES(?, ?, ?, ?);";
    public static final String UPDATE_USER = "UPDATE users SET login = ?, name = ?, email = ?, birth_day = ?;";
    public static final String GET_USERS_FRIENDS = "SELECT u.USER_ID,"+
            "u.LOGIN," +
            "u.NAME," +
            "u.EMAIL," +
            "u.BIRTH_DAY" +
            "FROM USERS AS u" +
            "INNER JOIN FRIENDS AS f ON f.FRIEND_USER_ID = u.USER_ID" +
            "GROUP BY u.USER_ID , u.LOGIN , u.NAME , u.EMAIL , u.BIRTH_DAY" +
            "HAVING f.USER_ID = ?;";
    public static final String GET_COMMON_FRIENDS = "SELECT * FROM USERS AS u WHERE u.USER_ID IN" +
            "(" +
                "SELECT f1.FRIEND_USER_ID" +
                    "FROM (" +
                        "SELECT FRIEND_USER_ID" +
                        "FROM FRIENDS AS f" +
                        "WHERE USER_ID = ?" +
                        ") AS f1" +
                    "JOIN (" +
                        "SELECT FRIEND_USER_ID" +
                        "FROM FRIENDS AS fr" +
                        "WHERE USER_ID = ?" +
                        ") AS f2 ON f2.FRIEND_USER_ID = f1.FRIEND_USER_ID)";




}
