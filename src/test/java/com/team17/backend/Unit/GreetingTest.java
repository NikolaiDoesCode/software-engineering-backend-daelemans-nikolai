package com.team17.backend.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// import java.util.Set;
// import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team17.backend.Greeting.model.Greeting;

public class GreetingTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String validMessage = "Hello, World!";
    private Greeting validGreeting;

    @BeforeAll
    public static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    public static void close() {
        validatorFactory.close();
    }

    @BeforeEach
    public void setUp() {
        validGreeting = new Greeting(validMessage);
    }

    @Test
    public void givenValidMessage_whenCreatingGreeting_thenGreetingIsCreatedWithMessage() {
        assertNotNull(validGreeting);
        assertEquals(validMessage, validGreeting.getMessage());
    }
}
