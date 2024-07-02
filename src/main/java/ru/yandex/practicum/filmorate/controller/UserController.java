package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")

public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User newUser) {
        log.info("Обработка метода POST для пользователя {}", newUser);
        if (newUser.getLogin().contains(" ")) {
            log.warn("Login пользователя ({}) содержит пробелы. Пользователь не содан", newUser.getLogin());
            throw new ValidationException("Login содержит пробелы");
        }
        newUser.setId(getNextId());
        log.debug("Пользователю {} присвоен ID {}", newUser.getName(), newUser.getId());
        if (!StringUtils.hasText(newUser.getName())) {
            newUser.setName(newUser.getLogin());
            log.debug("У пользователя с логином {} отсутствует имя. Имя будет присвоено в соответствии с логином",
                    newUser.getLogin());
        }

        users.put(newUser.getId(), newUser);
        log.debug("Пользователь c логином {} и именем {} сохранён", newUser.getLogin(), newUser.getName());
        return newUser;

    }

    @PutMapping
    public User update(@Valid @RequestBody User updatedUser) {
        log.info("Обработка метода PUT для пользователя {}", updatedUser);
        if (users.containsKey(updatedUser.getId())) {
            if (updatedUser.getLogin().contains(" ")) {
                log.warn("Login пользователя ({}) содержит пробелы. Пользователь не обновлён", updatedUser.getLogin());
                throw new ValidationException("Login содержит пробелы");
            }
            if (updatedUser.getName().isBlank()) {
                updatedUser.setName(updatedUser.getLogin());
                log.debug("У пользователя с логином {} отсутствует имя. Имя обновлено в соответствии с логином",
                        updatedUser.getLogin());
            }
            User oldUser = users.get(updatedUser.getId());
            oldUser.setBirthday(updatedUser.getBirthday());
            oldUser.setLogin(updatedUser.getLogin());
            oldUser.setEmail(updatedUser.getEmail());
            oldUser.setName(updatedUser.getName());
            log.debug("Пользователь c логином {} и именем {} обновлён", updatedUser.getLogin(), updatedUser.getName());
            return oldUser;
        } else {
            log.warn("Пользователь с ID {} в базе не найден", updatedUser.getId());
            throw new ValidationException("Пользователь с таким ID для обновления не найден");
        }

    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}
