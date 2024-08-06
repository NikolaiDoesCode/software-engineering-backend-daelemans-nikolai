package com.team17.backend.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team17.backend.Billing.model.Billing;
import com.team17.backend.Car.model.Car;
import com.team17.backend.User.model.User;

public class BillingTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private User validOwner;
    private User validRenter;
    private Car validCar;
    private LocalDate validStartDate;
    private Billing validBilling;

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
        validOwner = new User();
        validRenter = new User();
        validCar = new Car();
        validStartDate = LocalDate.now();
        validBilling = new Billing(validOwner, validRenter, validCar, validStartDate);
    }

    @Test
    public void givenValidValues_whenCreatingBilling_thenBillingIsCreatedWithThoseValues() {
        assertNotNull(validBilling);
        assertEquals(validOwner, validBilling.owner);
        assertEquals(validRenter, validBilling.renter);
        assertEquals(validCar, validBilling.car);
        assertEquals(validStartDate, validBilling.startDate);
    }
    
    @Test
    public void givenValidCost_whenSettingCost_thenCostIsSet() {
        float cost = 100.0f;
        validBilling.setCost(cost);
        assertEquals(cost, validBilling.cost);
    }

    @Test
    public void givenValidEndDate_whenSettingEndDate_thenEndDateIsSet() {
        validBilling.setEndDate();
        assertEquals(LocalDate.now(), validBilling.endDate);
    }
}
