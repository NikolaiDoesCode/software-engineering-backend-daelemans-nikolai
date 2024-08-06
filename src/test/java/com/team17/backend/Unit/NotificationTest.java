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

import com.team17.backend.Car.model.Car;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.model.Notification.NotificationStatus;

public class NotificationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    private long validRentId = 1L;
    private String validBrand = "BMW";
    private Car.CarType validType = Car.CarType.COUPE;
    private String validLicensePlate = "KU-D8H";
    private LocalDate validStartDate = LocalDate.now().plusDays(1); 
    private LocalDate validEndDate = LocalDate.now().plusDays(7); 
    private String validEmailOwner = "owner@example.com";
    private String validEmailRenter = "renter@example.com";
    private Notification.NotificationType validNotificationType = Notification.NotificationType.RENT;
    private Notification validNotification;
    private NotificationStatus validNotificationStatus = NotificationStatus.UNREAD;

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
        validNotification = new Notification(validRentId, validBrand, validType, validLicensePlate, validStartDate,
                validEndDate, validEmailOwner, validEmailRenter, validNotificationType, validNotificationStatus);
    }

    @Test
    public void givenValidValues_whenCreatingNotification_thenNotificationIsCreatedWithThoseValues() {
        assertNotNull(validNotification);
        assertEquals(validRentId, validNotification.getRentId());
        assertEquals(validBrand, validNotification.getBrand());
        assertEquals(validType, validNotification.getType());
        assertEquals(validLicensePlate, validNotification.getLicensePlate());
        assertEquals(validStartDate, validNotification.getStartDate());
        assertEquals(validEndDate, validNotification.getEndDate());
        assertEquals(validEmailOwner, validNotification.getEmailOwner());
        assertEquals(validEmailRenter, validNotification.getEmailRenter());
        assertEquals(validNotificationType, validNotification.getNotificationType());
    }

    @Test
    public void givenInvalidStartDate_whenCreatingNotification_thenStartDateViolationMessageIsThrown() {
        LocalDate pastStartDate = LocalDate.now().minusDays(1);
        Notification notification = new Notification(validRentId, validBrand, validType, validLicensePlate,
                pastStartDate, validEndDate, validEmailOwner, validEmailRenter, validNotificationType, validNotificationStatus);

        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertEquals(1, violations.size());
        ConstraintViolation<Notification> violation = violations.iterator().next();
        assertEquals("Start date is invalid, it has to be in the future", violation.getMessage());
    }

    @Test
    public void givenInvalidEmailOwner_whenCreatingNotification_thenEmailOwnerViolationMessageIsThrown() {
        String invalidEmailOwner = "invalid_email"; 
        Notification notification = new Notification(validRentId, validBrand, validType, validLicensePlate,
                validStartDate, validEndDate, invalidEmailOwner, validEmailRenter, validNotificationType, validNotificationStatus);

        Set<ConstraintViolation<Notification>> violations = validator.validate(notification);
        assertEquals(1, violations.size());
        ConstraintViolation<Notification> violation = violations.iterator().next();
        assertEquals("Email value is invalid, it has to be of the following format xxx@yyy.zzz", violation.getMessage());
    }

}
