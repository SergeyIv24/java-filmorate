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

    static final String UPDATE_FILM = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
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
            "INNER JOIN RATINGS r ON r.RATING_ID = f.RATING_ID " +
            "ORDER BY FILMS_ID";

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
            "rat.NAME AS Mpa_name, " +
            "FROM FILMS AS f " +
            "LEFT JOIN ( " +
            "SELECT FILMS_ID, COUNT (USER_ID) AS popular " +
            "FROM WHO_LIKED AS WL " +
            "GROUP BY FILMS_ID " +
            "ORDER BY popular DESC " +
            //"LIMIT ? " +
            ") AS pop ON pop.FILMS_ID = f.FILMS_ID " +
            "INNER JOIN ( " +
            "SELECT * " +
            "FROM RATINGS AS r " +
            ") AS rat ON rat.RATING_ID = f.RATING_ID ";

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

    static final String ADD_DIRECTOR = "INSERT INTO DIRECTORS (NAME) " +
            "VALUES (?)";

    static final String UPDATE_DIRECTOR = "UPDATE DIRECTORS SET NAME = ? " +
            "WHERE DIRECTOR_ID = ?";

    static final String GET_ALL_DIRECTORS = "SELECT * FROM DIRECTORS";

    static final String GET_DIRECTOR_BY_ID = "SELECT * " +
            "FROM DIRECTORS " +
            "WHERE DIRECTOR_ID = ?";

    static final String DELETE_DIRECTOR = "DELETE FROM DIRECTORS " +
            "WHERE DIRECTOR_ID = ?";

    static final String ADD_DIRECTORS_TO_FILM = "INSERT INTO FILMS_DIRECTORS (DIRECTOR_ID, FILM_ID) " +
            "VALUES (?, ?)";

    static final String FIND_ALL_DIRECTORS_OF_FILM = "SELECT d.director_id, d.name " +
            "FROM FILMS_DIRECTORS fd " +
            "LEFT JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
            "WHERE fd.FILM_ID = ?";

    static final String GET_DIRECTOR_FILMS_SORT_BY_YEAR = "SELECT * " +
            "FROM FILMS_DIRECTORS fd " +
            "JOIN FILMS f ON fd.FILM_ID = f.FILMS_ID " +
            "WHERE fd.DIRECTOR_ID = ? " +
            "ORDER BY f.RELEASE_DATE";

    static final String GET_DIRECTOR_FILMS_SORT_BY_LIKES = "SELECT f.*, COUNT(wl.USER_ID) AS likes_count " +
            "FROM FILMS_DIRECTORS fd " +
            "JOIN FILMS f ON fd.FILM_ID = f.FILMS_ID " +
            "LEFT JOIN WHO_LIKED wl ON fd.FILM_ID = wl.FILMS_ID " +
            "WHERE fd.DIRECTOR_ID = ? " +
            "GROUP BY fd.FILM_ID " +
            "ORDER BY LIKES_COUNT DESC";

    static final String ADD_REVIEW = "INSERT INTO reviews (content, isPositive, user_id, films_id) " +
            "VALUES (?, ?, ?, ?);";

    static final String UPDATE_REVIEW = "UPDATE reviews " +
            "SET content = ?, isPositive = ?, user_id = ?, films_id = ?, useful = ? " +
            "WHERE review_id = ?;";

    static final String DELETE_REVIEW = "DELETE FROM reviews " +
            "WHERE review_id = ?;";

    static final String REVIEW_BY_ID = "SELECT * FROM reviews " +
            "WHERE review_id = ?;";

    static final String GET_ALL_REVIEWS = "SELECT * FROM reviews ORDER BY USEFUL DESC LIMIT ?;";

    static final String GET_ALL_REVIEW_NO_LIMIT = "SELECT * FROM reviews ORDER BY USEFUL DESC;";

    static final String GET_ALL_FILMS_REVIEWS = "SELECT * FROM reviews " +
            "WHERE films_id = ? " +
            "ORDER BY USEFUL DESC " +
            "LIMIT ?;";

    static final String ADD_LIKE_TO_REVIEW = "UPDATE reviews " +
            "SET useful = (SELECT useful FROM reviews " +
            "WHERE review_id = ?) + 1 " +
            "WHERE review_id = ?;";

    static final String ADD_DISLIKE_TO_REVIEW = "UPDATE reviews " +
            "SET useful = (SELECT useful FROM reviews " +
            "WHERE review_id = ?) - 1 " +
            "WHERE review_id = ?;";

    static final String DELETE_OLD_DIRECTORS = "DELETE FROM FILMS_DIRECTORS WHERE FILM_ID = ?";

    static final String DELETE_OLD_GENERS = "DELETE FROM FILMS_GENERS WHERE FILMS_ID = ?";

    static final String INSERT_USERS_ACTION_IN_FEED = "INSERT INTO FEED (TIMESTAMP, USER_ID," +
            "EVENT_TYPE, OPERATION, entity_id) " +
            "VALUES (CURRENT_TIMESTAMP, ?, ?, ?, ?);";

    static final String GET_USERS_FEED = "SELECT EVENT_ID, " +
            "TIMESTAMP, " +
            "USER_ID, " +
            "e.EVENT_TYPE, " +
            "o.OPERATION, " +
            "entity_id " +
            "FROM FEED AS f " +
            "INNER JOIN EVENT_TYPES AS e ON f.EVENT_TYPE = e.TYPE_ID " +
            "INNER JOIN OPERATIONS AS o ON f.OPERATION  = o.OPERATION_ID " +
            "WHERE f.USER_ID = ?; ";

    static final String GET_COMMON_FILMS = "SELECT f.films_id, " +
            "f.NAME, " +
            "f.DESCRIPTION, " +
            "f.RELEASE_DATE, " +
            "f.DURATION, " +
            "f.RATING_ID, " +
            "pop.popular," +
            "r.NAME AS Mpa_name, " +
            "FROM FILMS AS f " +
            "LEFT JOIN RATINGS AS r ON f.RATING_ID  = r.RATING_ID " +
            "LEFT JOIN ( " +
            "SELECT films_id, COUNT (USER_ID) AS popular " +
            "FROM WHO_LIKED AS WL " +
            "GROUP BY FILMS_ID ) as pop ON pop.films_id = f.films_id " +
            "JOIN who_liked w1 ON f.films_id = w1.films_id AND w1.user_id = ? " +
            "JOIN who_liked w2 ON f.films_id = w2.films_id AND w2.user_id = ? " +
            "ORDER BY pop.popular DESC ";


    public static final String GET_FILMS_BY_USER_ID = "SELECT f.films_id, " +
            "f.name, " +
            "f.description, " +
            "f.release_date, " +
            "f.duration, " +
            "f.rating_id, " +
            "r.name as Mpa_name " +
            "FROM films f " +
            "JOIN who_liked w ON f.films_id = w.films_id " +
            "JOIN users u ON w.user_id = u.user_id " +
            "JOIN ratings r ON f.rating_id = r.rating_id " +
            "WHERE u.user_id = ?";

    static final String SEARCH_FILM_BY_TITLE = "SELECT f.*, COUNT(wl.USER_ID) AS likes_count, rat.name as Mpa_name  " +
            "FROM FILMS f " +
            "LEFT JOIN WHO_LIKED wl ON f.films_id = wl.FILMS_ID " +
            "INNER JOIN ( " +
            "SELECT * " +
            "FROM RATINGS AS r " +
            ") AS rat ON rat.RATING_ID = f.RATING_ID " +
            "WHERE LOWER(f.NAME) LIKE ? " +
            "GROUP BY f.films_id "+
            "ORDER BY likes_count DESC";

    static final String SEARCH_FILM_BY_DIRECTOR = "SELECT f.*, COUNT(wl.USER_ID) AS likes_count, rat.name as Mpa_name " +
            "FROM FILMS_DIRECTORS fd " +
            "JOIN FILMS f ON fd.film_id = f.FILMS_ID " +
            "LEFT JOIN WHO_LIKED wl ON fd.film_id = wl.FILMS_ID " +
            "LEFT JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
            "INNER JOIN ( " +
            "SELECT * " +
            "FROM RATINGS AS r " +
            ") AS rat ON rat.RATING_ID = f.RATING_ID " +
            "WHERE LOWER(d.NAME) LIKE ? " +
            "GROUP BY fd.film_id "+
            "ORDER BY likes_count DESC";

    static final String SEARCH_FILM_BY_DIRECTOR_AND_NAME = "SELECT f.*, COUNT(wl.USER_ID) AS likes_count, rat.name as Mpa_name " +
            "FROM FILMS f " +
            "LEFT JOIN WHO_LIKED wl ON f.films_id = wl.FILMS_ID " +
            "LEFT JOIN FILMS_DIRECTORS fd ON fd.film_id = f.FILMS_ID " +
            "LEFT JOIN DIRECTORS d ON fd.DIRECTOR_ID = d.DIRECTOR_ID " +
            "INNER JOIN ( " +
            "SELECT * " +
            "FROM RATINGS AS r " +
            ") AS rat ON rat.RATING_ID = f.RATING_ID " +
            "WHERE LOWER(d.NAME) LIKE ? OR LOWER(f.NAME) LIKE ?" +
            "GROUP BY f.films_id "+
            "ORDER BY likes_count DESC";
}
