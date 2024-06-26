package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.controller.Events;
import ru.yandex.practicum.filmorate.controller.Operations;
import ru.yandex.practicum.filmorate.model.Feed;

import java.util.Collection;

@Repository
public class FeedDbStorage extends BaseStorage<Feed> {
    private static final Logger log = LoggerFactory.getLogger(FeedDbStorage.class);

    public FeedDbStorage(JdbcTemplate template, RowMapper<Feed> mapper) {
        super(template, mapper);
    }

    public void addUserActivity(Long userId, Long entityId, Events event, Operations operation) {
        insert(SQLqueries.INSERT_USERS_ACTION_IN_FEED, userId, event.getEvent(), operation.getOperation(), entityId);
    }

    public Collection<Feed> getUsersFeed(Long userId) {
        return getAllItems(SQLqueries.GET_USERS_FEED, userId);
    }
}
