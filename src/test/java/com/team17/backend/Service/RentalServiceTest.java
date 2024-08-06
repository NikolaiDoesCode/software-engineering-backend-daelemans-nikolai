package com.team17.backend.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Executable;
import java.time.LocalDate;
import java.time.LocalTime;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.service.CarServiceException;
import com.team17.backend.Rent.service.RentService;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.model.RentalException;
import com.team17.backend.Rental.service.RentalRepository;
import com.team17.backend.Rental.service.RentalService;
import com.team17.backend.Rental.service.RentalServiceException;
import com.team17.backend.User.model.LoginResponse;
import com.team17.backend.User.model.Role;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserRepository;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {

    @Mock
    RentalRepository rentalRepository;

    @Mock
    RentalService rentalService;

    @InjectMocks
    RentService rentService;

    private Rental rental1;
    private Rental rental2;

    private long carId = 1;

    private LocalDate startDate1 = LocalDate.of(2024, 8, 11);
    private LocalDate endDate1 = LocalDate.of(2024, 8, 15);
    private LocalTime startTime1 = LocalTime.of(12, 0, 0); 
    private LocalTime endTime1 = LocalTime.of(14, 0, 0); 

    private String city = "Leuven";
    private String phoneNumber = "1234567890";
    private String email = "owner1@owner.com";
    private String street = "Bondgenotenlaan";
    private int number = 1;
    private int postal = 1;
    private int price = 1;
    

    @BeforeEach
    public void setUp() throws RentalServiceException, RentalException {
        rental1 = new Rental(startDate1, startTime1, endDate1, endTime1, city, phoneNumber, email, street, number, postal, price);
        rental2 = new Rental(startDate1, startTime1, endDate1, endTime1, city, phoneNumber, email, street, number, postal, price);
    }

    // getRentalsWithoutRentedOne 
    @Test
    public void givenExistingRentals_whenGetAllRentals_thenListOfRentalsIsReturned() {
        // Given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rental1);
        
        when(rentalService.findAllRentalsWithoutRentedOnes()).thenReturn(rentals);

        // When
        List<Rental> foundRentals = rentalService.findAllRentalsWithoutRentedOnes();

        // Then
        assertEquals(1, foundRentals.size());
        assertEquals(rental1, foundRentals.get(0));
    }

    // findAllRentalsOfCar
    @Test
    public void givenExistingRentals_whenGetAllRentalsOfCar_thenListOfRentalsIsReturned() {
        // Given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rental1);
        
        when(rentalService.findAllRentalsOfCar(carId)).thenReturn(rentals);

        // When
        List<Rental> foundRentals = rentalService.findAllRentalsOfCar(carId);

        // Then
        assertEquals(1, foundRentals.size());
        assertEquals(rental1, foundRentals.get(0));
    }

    // searchRentalsOnEmail
    @Test
    public void givenExistingRentals_whenSearchingRentalsOnEmail_thenListOfRentalsIsReturned() throws RentalServiceException {
        // Given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rental1);
        
        when(rentalService.searchRentals(email, startDate1, endDate1, "Toyota")).thenReturn(rentals);

        // When
        List<Rental> foundRentals = rentalService.searchRentals(email, startDate1, endDate1, "Toyota");

        // Then
        assertEquals(1, foundRentals.size());
        assertEquals(rental1, foundRentals.get(0));
    }

    // searchRentalsOnStartDate
    @Test
    public void givenNoRentals_whenSearchingRentals_thenErrorIsThrowned() throws RentalServiceException {
        // Given
        when(rentalService.searchRentals(email, startDate1, endDate1, "Toyota"))
                .thenThrow(new RentalServiceException("Combination", "There are no rentals that satisfy the combination of values provided"));

        //when
        RentalServiceException ex = Assertions.assertThrows(RentalServiceException.class, ()-> rentalService.searchRentals(email, startDate1, endDate1, "Toyota"));

        // Then
        assertEquals("Combination", ex.getField());
        assertEquals("There are no rentals that satisfy the combination of values provided", ex.getMessage());
    }

    // searchRentalsOnStartDate
    @Test
    public void givenExistingRentals_whenSearchingRentalsOnStartDate_thenErrorIsThrowned() throws RentalServiceException {
        // Given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rental1);
        
        when(rentalService.searchRentals(email, startDate1, endDate1, "Toyota"))
                .thenThrow(new RentalServiceException("Combination", "There are no rentals that satisfy the combination of values provided"));

        //when
        RentalServiceException ex = Assertions.assertThrows(RentalServiceException.class, ()-> rentalService.searchRentals(email, startDate1, endDate1, "Toyota"));

        // Then
        assertEquals("Combination", ex.getField());
        assertEquals("There are no rentals that satisfy the combination of values provided", ex.getMessage());
    }

    // addRental happy case
    @Test
    public void givenNoRentals_whenAddingRental_thenRentalIsReturned() throws RentalServiceException, CarServiceException {
        // Given
        
        when(rentalService.addRentalToCar(carId, rental1)).thenReturn(rental1);

        // When
        Rental rental = rentalService.addRentalToCar(carId, rental1);

        // Then
        assertEquals(rental1, rental);
    }

    // addRental unhappy case
    @Test
    public void givenExistingRentals_whenAddingRentalOnExistingStartDate_thenErrorIsThrowed() throws RentalServiceException, CarServiceException {
        // Given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rental1);
        
        when(rentalService.addRentalToCar(carId, rental1))
        .thenThrow(new RentalServiceException("Start and end date", "The rental cannot overlap with another existing rental"));

        // When
        RentalServiceException ex = Assertions.assertThrows(RentalServiceException.class, ()-> rentalService.addRentalToCar(carId, rental1));


        // Then
        assertEquals("Start and end date", ex.getField());
        assertEquals("The rental cannot overlap with another existing rental", ex.getMessage());
    }

    // addRental unhappy case
    @Test
    public void givenExistingRentals_whenAddingRentalWithStartDateAfterEndDate_thenErrorIsThrowed() throws RentalServiceException, CarServiceException {
        // Given
        List<Rental> rentals = new ArrayList<>();
        rentals.add(rental1);
        
        when(rentalService.addRentalToCar(carId, rental1))
        .thenThrow(new RentalServiceException("Start date", "Start date must be before the end date"));

        // When
        RentalServiceException ex = Assertions.assertThrows(RentalServiceException.class, ()-> rentalService.addRentalToCar(carId, rental1));


        // Then
        assertEquals("Start date", ex.getField());
        assertEquals("Start date must be before the end date", ex.getMessage());
    }
}