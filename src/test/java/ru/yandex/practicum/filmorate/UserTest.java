package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidUser() {

        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validlogin");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 1, 1));

        var violations = validator.validate(user);

        assertEquals(0, violations.size());
    }

    @Test
    void testUserWithEmptyEmail() {

        User user = new User();
        user.setLogin("validlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        var violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage()
                        .contains("Электронная почта не может быть пустой")));
    }

    @Test
    void testUserWithInvalidEmail() {

        User user = new User();
        user.setEmail("invalid-email");
        user.setLogin("validlogin");
        user.setBirthday(LocalDate.now().minusYears(20));

        var violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage()
                        .contains("Электронная почта должна быть корректного формата")));
    }

    @Test
    void testUserWithEmptyLogin() {

        User user = new User();
        user.setEmail("user@example.com");
        user.setBirthday(LocalDate.now().minusYears(20));

        var violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage()
                        .contains("Логин не может быть пустым")));
    }

    @Test
    void testUserWithSpacesInLogin() {

        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("user with spaces");
        user.setBirthday(LocalDate.now().minusYears(20));

        var violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage()
                        .contains("Логин не должен содержать пробелы")));
    }

    @Test
    void testUserWithFutureBirthday() {

        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("validlogin");
        user.setBirthday(LocalDate.now().plusDays(1));

        var violations = validator.validate(user);

        assertEquals(1, violations.size());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage()
                        .contains("Дата рождения не может быть в будущем")));
    }

    @Test
    void testUserWithNullNameUsesLogin() {

        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("john_doe");
        user.setName(null);
        user.setBirthday(LocalDate.of(1990, 1, 1));

        var violations = validator.validate(user);

        assertEquals(0, violations.size());
    }
}
