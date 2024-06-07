package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class Operations {
    @JsonIgnore
    Integer operationId;
    String operation;
}
