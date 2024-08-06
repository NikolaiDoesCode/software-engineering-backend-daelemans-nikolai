package com.team17.backend.Unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.team17.backend.Car.model.Car;

public class CarTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String validBrand = "BMW";
    private String validModel = "X6";
    private Car.CarType validType = Car.CarType.COUPE;
    private String validLicensePlate = "KU-D8H";
    private int validNumberOfSeats = 5;
    private int validNumberOfChildSeats = 1;
    private boolean validFoldingRearSeat = true;
    private boolean validTowBar = false;
    private Car validCar;

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
        validCar = new Car(validBrand, validModel, validType, validLicensePlate, validNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);
    }

    @Test
    public void givenValidValues_whenCreatingCar_thenCarIsCreatedWithThoseValues() {
        assertNotNull(validCar);
        assertEquals(validBrand, validCar.getBrand());
        assertEquals(validModel, validCar.getModel());
        assertEquals(validType, validCar.getType());
        assertEquals(validLicensePlate, validCar.getLicensePlate());
        assertEquals(validNumberOfSeats, validCar.getNumberOfSeats());
        assertEquals(validNumberOfChildSeats, validCar.getNumberOfChildSeats());
        assertEquals(validFoldingRearSeat, validCar.getFoldingRearSeat());
        assertEquals(validTowBar, validCar.getTowBar());
    }

    @Test
    public void givenBlankBrand_whenCreatingCar_thenBrandViolationMessageIsThrown() {
        String emptyBrand = " ";
        Car car = new Car(emptyBrand, validModel, validType, validLicensePlate, validNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Brand is required", violation.getMessage());
    }

    @Test
    public void givenNullCarType_whenCreatingCar_thenCarViolationMessageIsThrown() {
        Car.CarType nullCarType = null;
        Car car = new Car(validBrand, validModel, nullCarType, validLicensePlate, validNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Type is required", violation.getMessage());
    }

    @Test
    public void givenBlankLicensePlate_whenCreatingCar_thenLicensePlateViolationMessageIsThrown() {
        String emptyLicensePlate = " ";
        Car car = new Car(validBrand, validModel, validType, emptyLicensePlate, validNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("License plate is required", violation.getMessage());
    }

    @Test
    public void givenZeroNumberOfSeats_whenCreatingCar_thenNumberOfSeatsViolationMessageIsThrown() {
        int zeroNumberOfSeats = 0;
        Car car = new Car(validBrand, validModel, validType, validLicensePlate, zeroNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);

        Set<ConstraintViolation<Car>> violations = validator.validate(car);
        assertEquals(1, violations.size());
        ConstraintViolation<Car> violation = violations.iterator().next();
        assertEquals("Number of seats is required", violation.getMessage());
    }

}
