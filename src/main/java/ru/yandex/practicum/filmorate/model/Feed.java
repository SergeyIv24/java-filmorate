package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.Instant;

@Data
public class Feed {
    Long event_id;
    @NotNull(message = "Временна метка должна быть указана")
    @PastOrPresent
    Instant timestamp;
    @NotNull(message = "User_id не может быть пустым")
    Long userId;
    @NotNull(message = "Event_type не может быть пустым")
    EventTypes event_type;
    @NotNull(message = "Operation не может быть пустым")
    Operations operation;
    @NotNull(message = "Entity_id не может быть пустым")
    Long entity_id;

/*    @JsonGetter("timestamp")
    public Long timestampToSecondsAmount() {
        return timestamp.toEpochMilli();
    }

    @JsonSetter("timestamp")
    public void timeStampToInstant(long time) {
        this.timestamp = Instant.ofEpochSecond(time);
    }*/

}
