package ru.yandex.practicum.filmorate.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import ru.yandex.practicum.filmorate.exception.ValidError;
import ru.yandex.practicum.filmorate.exception.ValidationException;

@ControllerAdvice
public class ValidExceptionHandler {
    @ExceptionHandler({ValidationException.class})
    protected ResponseEntity<Object> handleEntityNotFoundEx(Exception ex, WebRequest request) {
        ValidError validError = new ValidError("Ошибка валидации данных: ", ex.getMessage());
        return new ResponseEntity<>(validError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
