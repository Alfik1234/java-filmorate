package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();
    private Long nextId = 1L;

    private static final LocalDate CINEMA_BIRTHDAY = LocalDate.of(1895, 12, 28);

    private Long getNextId() {
        return nextId++;
    }

    private void validateFilm(Film film) {
        if (film.getReleaseDate().isBefore(CINEMA_BIRTHDAY)) {
            log.error("Дата релиза раньше 28.12.1895");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года");
        }
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        validateFilm(film);

        film.setId(getNextId());
        films.put(film.getId(), film);

        log.info("Добавлен фильм: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {

        if (!films.containsKey(film.getId())) {
            log.error("Фильм с id {} не найден", film.getId());
            throw new ValidationException("Фильм с id " + film.getId() + " не найден");
        }

        validateFilm(film);

        films.put(film.getId(), film);

        log.info("Обновлён фильм: {}", film.getName());
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Получен список фильмов");
        return new ArrayList<>(films.values());
    }
}
