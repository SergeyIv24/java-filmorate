package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.EventTypes;
import ru.yandex.practicum.filmorate.model.Feed;
import ru.yandex.practicum.filmorate.model.Operations;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FeedMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        EventTypes eventTypes = new EventTypes();
        Operations operations = new Operations();

        eventTypes.setEvent_type(rs.getString("event_type"));
        operations.setOperation(rs.getString("operation"));

        feed.setEvent_id(rs.getLong("event_id"));
        Timestamp timestamp = rs.getTimestamp("timestamp");
        feed.setTimestamp(timestamp.toInstant());
        feed.setUserId(rs.getLong("user_id"));
/*        feed.setEvent_type(eventTypes);
        feed.setOperation(operations);*/
        feed.setEventType(rs.getString("event_type"));
        feed.setOperation(rs.getString("operation"));
        feed.setEntityId(rs.getLong("entity_id"));
        return feed;
    }
}
