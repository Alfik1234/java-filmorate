package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private Long nextId = 1L;

    private Long getNextId() {
        return nextId++;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
        }

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Добавлен фильм {}", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм не найден");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895");
        }

        films.put(film.getId(), film);

        log.info("Обновлён фильм {}", film);
        return film;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Получен список фильмов");
        return new ArrayList<>(films.values());
    }
}
