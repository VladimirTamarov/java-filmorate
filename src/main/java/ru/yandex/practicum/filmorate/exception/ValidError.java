package ru.yandex.practicum.filmorate.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidError {
    private String message;
    private String debugMessage;
}
