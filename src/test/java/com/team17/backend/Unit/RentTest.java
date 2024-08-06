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

import com.team17.backend.Rent.model.Rent;
import com.team17.backend.Rent.service.RentServiceException;
import com.team17.backend.Rental.model.Rental;

public class RentTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private String validPhoneNumberRenter = "+1234567890";
    private String validEmailRenter = "renter@example.com";
    private String validNationalRegisterId = "YY.MM.DD-XXX.ZZ";
    private LocalDate validBirthDate = LocalDate.of(1990, 5, 15);
    private String validDrivingLicenseNumber = "1234567890";
    private Boolean validCheckedIn = false;
    private Rental validRental = new Rental();
    private Rent validRent;
    private Float validFuelCheckIn = 100f;
    private Float validFuelCheckOut = 100f;
    private Float validMilageCheckIn = 100f;
    private Float validMilageCheckOut = 100f;

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
    public void setUp() throws RentServiceException {
        validRent = new Rent(validPhoneNumberRenter, validEmailRenter, validNationalRegisterId, validBirthDate,
                validRental, validDrivingLicenseNumber, validCheckedIn, validFuelCheckIn, validFuelCheckOut,
                validMilageCheckIn, validMilageCheckOut);
    }

    @Test
    public void givenValidValues_whenCreatingRent_thenRentIsCreatedWithThoseValues() {
        assertNotNull(validRent);
        assertEquals(validPhoneNumberRenter, validRent.getPhoneNumberRenter());
        assertEquals(validEmailRenter, validRent.getEmailRenter());
        assertEquals(validNationalRegisterId, validRent.getNationalRegisterId());
        assertEquals(validBirthDate, validRent.getBirthDate());
        assertEquals(validDrivingLicenseNumber, validRent.getDrivingLicenseNumber());
        assertEquals(validCheckedIn, validRent.getCheckedIn());
        assertEquals(validRental, validRent.getRental());
        assertEquals(validRental, validRent.getRental());

        assertEquals(validFuelCheckIn, validRent.getFuelCheckIn());
        assertEquals(validFuelCheckOut, validRent.getFuelCheckOut());
        assertEquals(validMilageCheckIn, validRent.getMileageCheckIn());
        assertEquals(validMilageCheckOut, validRent.getMileageCheckOut());
    }

}
