package com.team17.backend.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Set;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class UserTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String validEmail = "test@example.com";
    private String validPassword = "password123";
    private String validFirstName = "John";
    private String validLastName = "Doe";
    private String validPhoneNumber = "+1234567890";
    private LocalDate validDateOfBirth = LocalDate.of(1990, 5, 15);
    private User validUser;
    private Role validOwnerRole = Role.OWNER;

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
    public void setUp() throws UserServiceException {
        validUser = new User(validEmail, validPassword, validFirstName, validLastName, validPhoneNumber,
                validDateOfBirth, validOwnerRole);
    }
    // S2 - Authentication - Create an account V

    @Test
    public void givenValidValues_whenCreatingUser_thenUserIsCreatedWithThoseValues() {
        assertNotNull(validUser);
        assertEquals(validEmail, validUser.getEmail());
        assertTrue(validUser.checkPwd(validPassword));
        assertEquals(validFirstName, validUser.getFirstName());
        assertEquals(validLastName, validUser.getLastName());
        assertEquals(validPhoneNumber, validUser.getPhoneNumber());
        assertEquals(validDateOfBirth, validUser.getDateOfBirth());
    }

    @Test
    public void givenBlankPassword_whenCreatingUser_thenPasswordViolationMessageIsThrown() throws UserServiceException {
        String emptyPassword = " ";
        UserServiceException ex = Assertions.assertThrows(UserServiceException.class, () -> {
            new User(validEmail, emptyPassword, validFirstName, validLastName, validPhoneNumber,
                    validDateOfBirth, validOwnerRole);
        });

        assertEquals("password", ex.getField());
        assertEquals("Password is required", ex.getMessage());
    }

    @Test
    public void givenBlankFirstName_whenCreatingUser_thenFirstNameViolationMessageIsThrown()
            throws UserServiceException {
        String emptyFirstName = " ";
        User user = new User(validEmail, validPassword, emptyFirstName, validLastName, validPhoneNumber,
                validDateOfBirth, validOwnerRole);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("First name is required", violation.getMessage());
    }

    @Test
    public void givenBlankLastName_whenCreatingUser_thenLastNameViolationMessageIsThrown() throws UserServiceException {
        String emptyLastName = " ";
        User user = new User(validEmail, validPassword, validFirstName, emptyLastName, validPhoneNumber,
                validDateOfBirth, validOwnerRole);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Last name is required", violation.getMessage());
    }

    @Test
    public void givenInvalidPhoneNumber_whenCreatingUser_thenPhoneNumberViolationMessageIsThrown()
            throws UserServiceException {
        String invalidPhoneNumber = "123";
        User user = new User(validEmail, validPassword, validFirstName, validLastName, invalidPhoneNumber,
                validDateOfBirth, validOwnerRole);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Phone number is not a valid format", violation.getMessage());
    }

    @Test
    public void givenFutureDateOfBirth_whenCreatingUser_thenDateOfBirthViolationMessageIsThrown()
            throws UserServiceException {
        LocalDate futureDateOfBirth = LocalDate.now().plusYears(1);
        User user = new User(validEmail, validPassword, validFirstName, validLastName, validPhoneNumber,
                futureDateOfBirth, validOwnerRole);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals(1, violations.size());
        ConstraintViolation<User> violation = violations.iterator().next();
        assertEquals("Date of birth must be in the past", violation.getMessage());
    }
}
