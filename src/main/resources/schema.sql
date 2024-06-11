DROP TABLE IF EXISTS REVIEWS, FEED, FILMS_DIRECTORS, FRIENDS, STATUS  , EVENT_TYPES , DIRECTORS, OPERATIONS,
FILMS_GENERS, GENERS, WHO_LIKED, FILMS , USERS, RATINGS;

CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    login varchar NOT NULL,
    name varchar NOT NULL,
    email varchar NOT NULL,
    birth_day date NOT NULL
);

CREATE TABLE IF NOT EXISTS status (
    status_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    status_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS friends (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    friend_user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    status_id INTEGER REFERENCES status (status_id)
);

CREATE TABLE IF NOT EXISTS ratings (
    rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS geners (
    gener_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    gener_name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS films (
    films_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar NOT NULL,
    release_date date NOT NULL,
    duration INTEGER NOT NULL,
    rating_id INTEGER REFERENCES ratings (rating_id) NOT NULL
);

CREATE TABLE IF NOT EXISTS films_geners (
    films_id BIGINT REFERENCES films (films_id) ON DELETE CASCADE,
    gener_id BIGINT REFERENCES geners (gener_id),
    PRIMARY KEY (films_id, gener_id)
);

CREATE TABLE IF NOT EXISTS who_liked (
    user_id BIGINT REFERENCES users (user_id) ON DELETE CASCADE,
    films_id BIGINT REFERENCES films (films_id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, films_id)
);

CREATE TABLE IF NOT EXISTS reviews (
 	review_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
 	content varchar NOT NULL,
	isPositive bool NOT NULL,
	user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
	films_id BIGINT REFERENCES films (films_id) ON DELETE CASCADE,
 	useful BIGINT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS directors (
    director_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS films_directors (
    film_id BIGINT REFERENCES films (films_id) ON DELETE CASCADE,
    director_id BIGINT REFERENCES directors (director_id) ON DELETE CASCADE,
    PRIMARY KEY (director_id, film_id)
);
CREATE TABLE IF NOT EXISTS event_types (
    type_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    event_type varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS operations (
    operation_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    operation varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS feed (
    event_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    timestamp timestamp NOT NULL,
    user_id BIGINT NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
    event_type INTEGER NOT NULL REFERENCES event_types (type_id) ON DELETE CASCADE,
    operation INTEGER NOT NULL REFERENCES operations (operation_id) ON DELETE CASCADE,
    entity_id INTEGER NOT NULL
);
