package ru.yandex.practicum.filmorate.storage;

public class SQLqueries {
    static final String GET_ALL_USERS = "SELECT * FROM users;";
    static final String GET_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?;";
    static final String ADD_USER = "INSERT INTO users(login, name, email, birth_day)" +
            "VALUES(?, ?, ?, ?);";
    static final String UPDATE_USER = "UPDATE users SET login = ?, name = ?, email = ?, birth_day = ? WHERE USER_ID = ?";
    static final String DELETE_USER = "DELETE FROM users WHERE user_id = ?";
    static final String GET_USERS_FRIENDS = "SELECT u.USER_ID, " +
            "u.LOGIN, " +
            "u.NAME, " +
            "u.EMAIL, " +
            "u.BIRTH_DAY " +
            "FROM USERS AS u " +
            "INNER JOIN FRIENDS AS f ON f.FRIEND_USER_ID = u.USER_ID " +
            "GROUP BY u.USER_ID, f.USER_ID, u.LOGIN , u.NAME , u.EMAIL , u.BIRTH_DAY " +
            "HAVING f.USER_ID = ?; ";

    static final String GET_COMMON_FRIENDS = "SELECT * FROM USERS AS u WHERE u.USER_ID IN " +
            "(" +
                "SELECT f1.FRIEND_USER_ID " +
                    "FROM (" +
                        "SELECT FRIEND_USER_ID " +
                        "FROM FRIENDS AS f " +
                        "WHERE USER_ID = ? AND STATUS_ID = 1 " +
                        ") AS f1 " +
                    "JOIN ( " +
                        "SELECT FRIEND_USER_ID " +
                        "FROM FRIENDS AS fr " +
                        "WHERE USER_ID = ? AND STATUS_ID = 1 " +
                        ") AS f2 ON f2.FRIEND_USER_ID = f1.FRIEND_USER_ID) ";

    static final String MAKE_FRIENDS = "INSERT INTO FRIENDS (USER_ID, FRIEND_USER_ID, STATUS_ID) " +
            "VALUES (?, ?, 1)";

    static final String DELETE_FRIEND = "DELETE FROM FRIENDS " +
            "WHERE USER_ID = ? " +
            "AND FRIEND_USER_ID = ?;";


    static final String ADD_FILM = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
            "VALUES(?, ?, ?, ?, ?)";

    static final String UPDATE_FILM = "UPDATE films SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
            "RATING_ID = ? " +
            "WHERE FILMS_ID = ?";

    static final String DELETE_FILM = "DELETE FROM films WHERE films_id = ?";

    static final String GET_FILM_BY_ID = "SELECT f.FILMS_ID, " +
            "f.NAME, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.RATING_ID, " +
            "r.NAME AS Mpa_name  " +
            "FROM FILMS AS f " +
            "INNER JOIN RATINGS AS r ON f.RATING_ID  = r.RATING_ID " +
            "WHERE f.FILMS_ID = ?";

    static final String GET_ALL_FILMS = "SELECT f.FILMS_ID, " +
            "f.NAME, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE,  " +
            "f.DURATION, " +
            "f.RATING_ID, " +
            "r.NAME AS Mpa_name " +
            "FROM films AS f " +
            "INNER JOIN RATINGS r ON r.RATING_ID = f.RATING_ID  ";

    static final String ADD_LIKE_TO_FILM = "INSERT INTO WHO_LIKED (FILMS_ID, USER_ID) " +
            "VALUES (?, ?)";

    static final String DELETE_LIKE = "DELETE FROM WHO_LIKED " +
            "WHERE FILMS_ID = ?" +
            "AND USER_ID = ?";

    static final String GET_POPULAR = "SELECT f.FILMS_ID," +
            "f.NAME, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.RATING_ID, " +
            "rat.NAME AS Mpa_name " +
            "FROM FILMS AS f " +
            "INNER JOIN ( " +
                "SELECT FILMS_ID, COUNT (USER_ID) AS popular " +
                "FROM WHO_LIKED AS WL " +
                "GROUP BY FILMS_ID " +
                "ORDER BY popular DESC " +
                "LIMIT ? " +
            ") AS pop ON pop.FILMS_ID = f.FILMS_ID " +
            "INNER JOIN ( " +
                "SELECT * " +
                "FROM RATINGS AS r " +
            ") AS rat ON rat.RATING_ID = f.RATING_ID  ";

    static final String ADD_GENRE = "INSERT INTO FILMS_GENERS (GENER_ID, FILMS_ID) " +
            "VALUES (?, ?)";

    static final String FIND_ALL_GENRES_OF_FILM = "SELECT fg.FILMS_ID, " +
            "fg.GENER_ID, " +
            "g.GENER_NAME  " +
            "FROM FILMS_GENERS AS fg " +
            "INNER JOIN GENERS AS g ON g.GENER_ID  = FG.GENER_ID  " +
            "WHERE FILMS_ID = ?";

    static final String GET_GENRE_BY_ID = "SELECT *  " +
            "FROM GENERS " +
            "WHERE GENER_ID = ? ";

    static final String GET_ALL_GENRES = "SELECT * FROM GENERS";

    static final String GET_ALL_MPA = "SELECT * FROM RATINGS";

    static final String GET_MPA_BY_ID = "SELECT *  " +
            "FROM RATINGS " +
            "WHERE RATING_ID = ?";
}
