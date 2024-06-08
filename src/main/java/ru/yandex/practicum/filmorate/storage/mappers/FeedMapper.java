package ru.yandex.practicum.filmorate.storage.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Feed;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Component
public class FeedMapper implements RowMapper<Feed> {
    @Override
    public Feed mapRow(ResultSet rs, int rowNum) throws SQLException {
        Feed feed = new Feed();
        feed.setEvent_id(rs.getLong("event_id"));
        Timestamp timestamp = rs.getTimestamp("timestamp");
        feed.setTimestamp(timestamp.toInstant());
        feed.setUserId(rs.getLong("user_id"));
        feed.setEventType(rs.getString("event_type"));
        feed.setOperation(rs.getString("operation"));
        feed.setEntityId(rs.getLong("entity_id"));
        return feed;
    }
}
