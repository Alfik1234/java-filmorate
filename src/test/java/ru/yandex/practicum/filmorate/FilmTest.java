package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FilmTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidFilm() {

        Film film = new Film();
        film.setName("Valid Film");
        film.setDescription("Short description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);

        var violations = validator.validate(film);

        assertEquals(0, violations.size());
    }

    @Test
    void testFilmWithEmptyName() {

        Film film = new Film();
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);

        var violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Название фильма не может быть пустым")));
    }

    @Test
    void testFilmWithLongDescription() {

        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("a".repeat(201));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(90);

        var violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage()
                .contains("Описание не может быть длиннее 200 символов")));
    }

    @Test
    void testFilmWithNullReleaseDate() {

        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setDuration(100);

        var violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Дата релиза не может быть пустой")));
    }

    @Test
    void testFilmWithNegativeDuration() {

        Film film = new Film();
        film.setName("Test Film");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-10);

        var violations = validator.validate(film);

        assertEquals(1, violations.size());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage()
                .contains("Продолжительность фильма должна быть положительным числом")));
    }
}
