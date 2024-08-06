
package com.team17.backend.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.model.Car.CarType;
import com.team17.backend.Car.service.CarRepository;
import com.team17.backend.Car.service.CarService;
import com.team17.backend.Car.service.CarServiceException;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.User.service.UserServiceException;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    CarRepository carRepository;

    @InjectMocks
    CarService carService;

    private Car car1;
    private Car car2;
    private Rental rental1;

    private String validBrand = "BMW";
    private String validModel = "X6";
    private CarType validType = CarType.COUPE;
    private String validLicensePlate = "KU-D8H";
    private int validNumberOfSeats = 5;
    private int validNumberOfChildSeats = 1;
    private boolean validFoldingRearSeat = true;
    private boolean validTowBar = false;

    @BeforeEach
    public void setUp() throws CarServiceException {
        car1 = new Car(validBrand, validModel, validType, validLicensePlate, validNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);
        car2 = new Car(validBrand, validModel, validType, validLicensePlate, validNumberOfSeats,
                validNumberOfChildSeats, validFoldingRearSeat, validTowBar);
    }

    @Test
    public void givenExistingCars_whenFindingAllCars_thenListOfCarsIsReturned() {
        // Given
        List<Car> cars = new ArrayList<>();
        cars.add(car1);
        cars.add(car2);
        when(carRepository.findAll()).thenReturn(cars);

        // When
        List<Car> foundCars = carService.findAllCars();

        // Then
        assertEquals(2, foundCars.size());
        assertEquals(car1, foundCars.get(0));
        assertEquals(car2, foundCars.get(1));
    }

    // happy case
    // @Test
    // public void givenNpCars_whenValidCarIsAdded_ThenCarIsAddedAndCarIsReturned()
    // throws CarServiceException, UserServiceException {
    // // given
    // when(carRepository.save(car1)).thenReturn(car1);

    // // when
    // Car added = carService.addCar(car1, "validEmail");

    // // Then
    // assertEquals(car1.getBrand(), added.getBrand());
    // assertEquals(car1.getModel(), added.getModel());
    // assertEquals(car1.getType(), added.getType());
    // assertEquals(car1.getLicensePlate(), added.getLicensePlate());
    // assertEquals(car1.getNumberOfSeats(), added.getNumberOfSeats());
    // assertEquals(car1.getNumberOfChildSeats(), added.getNumberOfChildSeats());
    // assertEquals(car1.getFoldingRearSeat(), added.getFoldingRearSeat());
    // assertEquals(car1.getTowBar(), added.getTowBar());
    // }

    // @Test
    // void givenNonExistingCar_whenAddingRental_thenCarServiceExceptionIsThrown() {
    // // Given
    // when(carRepository.findCarById(100)).thenReturn(null);

    // // When, Then
    // CarServiceException ex = assertThrows(CarServiceException.class, () ->
    // carService.addRental(100, rental1));

    // // Then
    // assertEquals("carId", ex.getField());
    // assertEquals("Car with id 100 does not exist", ex.getMessage());
    // }

    @Test
    void givenExistingCar_whenAddingRental_thenCarIsReturned() throws CarServiceException {
        // given
        when(carRepository.findCarById(1)).thenReturn(car1);
        // when
        Car foundCar = carRepository.findCarById(1);

        // Then
        assertEquals(car1.getBrand(), foundCar.getBrand());
        assertEquals(car1.getModel(), foundCar.getModel());
        assertEquals(car1.getType(), foundCar.getType());
        assertEquals(car1.getLicensePlate(), foundCar.getLicensePlate());
        assertEquals(car1.getNumberOfSeats(), foundCar.getNumberOfSeats());
        assertEquals(car1.getNumberOfChildSeats(), foundCar.getNumberOfChildSeats());
        assertEquals(car1.getFoldingRearSeat(), foundCar.getFoldingRearSeat());
        assertEquals(car1.getTowBar(), foundCar.getTowBar());
    }

    // @Test
    // void givenExistingCar_whenAddingRental_thenRentalIsAddedToCar() throws
    // CarServiceException {
    // // Given
    // long existingCarId = 1L;
    // when(carRepository.findCarById(existingCarId)).thenReturn(car1);

    // // When
    // Car updatedCar = carService.addRental(existingCarId, rental1);

    // // Then
    // verify(carRepository).findCarById(existingCarId);
    // assertEquals(rental1, updatedCar.getRental());
    // }

}
