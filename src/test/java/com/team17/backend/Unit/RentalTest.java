package com.team17.backend.Unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import java.time.LocalDate;
import java.time.LocalTime;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.model.RentalException;

import org.junit.jupiter.api.Test;

public class RentalTest {

    LocalDate startDate = LocalDate.now().plusDays(1);
    LocalDate endDate = LocalDate.now().plusDays(5);
    String city = "City";
    String phoneNumber = "+1234567890";
    String email = "user@example.com";
    String street = "Main St";
    int number = 123;
    int postal = 10000;
    Car car = mock(Car.class);
    LocalTime startTime = LocalTime.MIDNIGHT;
    LocalTime endTime = LocalTime.MIDNIGHT;

    @Test
    public void givenValidRental_whenCreatingRental_thenRentalIsCreatedWithThoseValues() throws RentalException {
        // When
        Rental rental = new Rental(startDate, startTime, endDate, endTime, city, phoneNumber, email, street, number,
                postal, number);
        // Then
        assertEquals(startDate, rental.getEndDate());
        assertEquals(endDate, rental.getStartDate());
        assertEquals(rental.getEndTime(), endTime);
        assertEquals(rental.getStartTime(), startTime);
        assertEquals(city, rental.getCity());
        assertEquals(phoneNumber, rental.getPhoneNumber());
        assertEquals(email, rental.getEmail());
        assertEquals(street, rental.getStreet());
        assertEquals(number, rental.getNumber());
        assertEquals(postal, rental.getPostal());
    }

}
