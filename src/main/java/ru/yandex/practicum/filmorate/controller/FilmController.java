package ru.yandex.practicum.filmorate.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@RestController
@RequestMapping("/films")

public class FilmController {

    private static final LocalDate earlyReleaseDate = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {

        return films.values();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Обработка метода POST для фильма {}", film);
        if (film.getReleaseDate().isBefore(earlyReleaseDate)) {
            log.warn("Дата релиза фильма ранее минимально возможной");
            throw new ValidationException("Неверные входные данные для создания фильма");

        }
        film.setId(getNextId());
        log.debug("Фильму с названием {} присвоен ID {}", film.getName(),film.getId());
        films.put(film.getId(), film);
        log.debug("Фильм {} сохранён", film.getName());
        return film;
    }


    @PutMapping
    public Film update(@Valid @RequestBody Film updatedFilm) {
        log.info("Обработка метода PUT для фильма {}", updatedFilm);
        if (films.containsKey(updatedFilm.getId())) {
            Film oldFilm = films.get(updatedFilm.getId());
            oldFilm.setName(updatedFilm.getName());
            oldFilm.setDescription(updatedFilm.getDescription());
            oldFilm.setDuration(updatedFilm.getDuration());
            oldFilm.setReleaseDate(updatedFilm.getReleaseDate());
            log.debug("Фильм {} обновлен", oldFilm.getName());
            return oldFilm;
        } else {
            log.warn("Фильм с ID {} в базе не найден", updatedFilm.getId());
            throw new ValidationException("ID фильма для обновлени отсутствует в списке");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

}
