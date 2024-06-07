package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class EventTypes {
    @JsonIgnore
    Integer typeId;
    String event_type;
}
