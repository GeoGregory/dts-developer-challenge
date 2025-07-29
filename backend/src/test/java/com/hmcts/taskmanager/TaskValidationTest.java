package com.hmcts.taskmanager;

import com.hmcts.taskmanager.model.Task;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void titleIsBlank() {
        Task task = new Task("   ", "desc", "pending", LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title")));
    }

    @Test
    void statusIsBlank() {
        Task task = new Task("Title", "desc", "   ", LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("status")));
    }

    @Test
    void dueDateIsNull() {
        Task task = new Task("Title", "desc", "pending", null);
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("dueDate")));
    }

    @Test
    void descriptionIsEmpty() {
        Task task = new Task("Valid Title", "", "done", LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertEquals(0, violations.size());
    }

    @Test
    void allFieldsAreValid() {
        Task task = new Task("Valid Title", "desc", "done", LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Task>> violations = validator.validate(task);

        assertEquals(0, violations.size());
    }
}

