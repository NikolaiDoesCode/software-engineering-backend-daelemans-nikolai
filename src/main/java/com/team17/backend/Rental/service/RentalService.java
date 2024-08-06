package com.team17.backend.Rental.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.service.CarService;
import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.service.CarRepository;
import com.team17.backend.Car.service.CarServiceException;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.Rent.service.RentService;
import com.team17.backend.Rental.model.Rental;

import jakarta.validation.Valid;

@Service
public class RentalService {

    @Autowired
    private CarService carService;

    @Autowired
    private RentService rentService;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private CarRepository carRepository;

    public RentalService() {
    }

    public List<Rental> findAllRentalsWithoutRentedOnes() {
        List<Rental> allRentals = rentalRepository.findAll();

        List<Rent> allRents = rentService.findAllRents();

        List<Rental> rentalsWithoutRents = new ArrayList<Rental>();

        for (Rental rental : allRentals) {
            boolean isUsed = false;
        
            for (Rent rent : allRents) {
                if (rent.getRental().equals(rental)) {
                    isUsed = true;
                    break;
                }
            }
        
            if (!isUsed) {
                rentalsWithoutRents.add(rental);
            }
        }

        return  rentalsWithoutRents;
    }

    public List<Rental> findAllRentalsWithRentedOnes() {
        return rentalRepository.findAll();
    }

    public List<Rental> findAllRentalsOfCar(Long carId) {
        List<Rental> allRentals = findAllRentalsWithRentedOnes();
        List<Rental> rentalsOfCar = new ArrayList<>();
        
        for (Rental rental : allRentals) {
            if (rental.getCar().id == carId) {
                rentalsOfCar.add(rental);
            }
        }

        return rentalsOfCar;
    }

    public List<Rental> searchRentals(String email, LocalDate startDate, LocalDate endDate, String brand)
            throws RentalServiceException {
        List<Rental> rentals = rentalRepository.searchRentals(email, startDate, endDate, brand);

        if (email == null && startDate == null && endDate == null && brand == null) {
            throw new RentalServiceException("Values",
                    "You need to choose one or more values to get search results");
        }

        if (rentals.isEmpty()) {
            if ((email != null ? 1 : 0) + (startDate != null ? 1 : 0) + (endDate != null ? 1 : 0) +
                    (brand != null ? 1 : 0) > 1) {
                throw new RentalServiceException("Combination",
                        "There are no rentals that satisfy the combination of values provided");
            }

            if (email != null && rentals.stream().noneMatch(rental -> rental.getEmail().equals(email))) {
                throw new RentalServiceException("Email",
                        "There are no rentals with this email address found as owner");
            }

            else if (brand != null && rentals.stream().noneMatch(rental -> rental.getCar().getBrand().equals(brand))) {
                throw new RentalServiceException("Brand",
                        "There are no rentals with this car brand found");
            }

            else if (startDate != null
                    && rentals.stream().noneMatch(rental -> rental.getStartDate().equals(startDate))) {
                throw new RentalServiceException("Start date",
                        "There are no rentals with this start date found");
            }

            else if (endDate != null && rentals.stream().noneMatch(rental -> rental.getEndDate().equals(endDate))) {
                throw new RentalServiceException("End date",
                        "There are no rentals with this end date found");
            }
        }

        return rentals;
    }

    public Rental addRentalToCar(Long carId, Rental rental) throws RentalServiceException, CarServiceException {
        Car car = carService.getCar(carId);
        if (car == null) {
            throw new CarServiceException("carId", "Car with id %s does not exist".formatted(carId));
        }   
        if (rental.getStartDate().isEqual(rental.getEndDate())) {
            if (rental.getStartDateTime() != null && rental.getEndDateTime() != null) {
                if (rental.getStartDateTime().isAfter(rental.getEndDateTime())) {
                    throw new RentalServiceException("Start time",
                    "Start time must be before the end time if the start date and the end date are the same day");
                }
            }
        }

        if (rental.getStartDate().isAfter(rental.getEndDate())) {
            throw new RentalServiceException("Start date",
                    "Start date must be before the end date");
        }

        List<Rental> rentalsOfCar = findAllRentalsOfCar(carId);

        for (Rental rentalOfCar : rentalsOfCar) {
            LocalDateTime startDatumOudeRental = rentalOfCar.getStartDateTime();
            LocalDateTime endDatumOudeRental = rentalOfCar.getEndDateTime();

            LocalDateTime startDatumNieuweRental = rental.getStartDateTime();
            LocalDateTime endDatumNieuweRental = rental.getEndDateTime();

            if (startDatumOudeRental.isEqual(startDatumNieuweRental)) {
                    throw new RentalServiceException("Start and end date",
                    "The rental cannot overlap with another existing rental");
            }

            if (endDatumOudeRental.isEqual(endDatumNieuweRental)) {
                throw new RentalServiceException("Start and end date",
                "The rental cannot overlap with another existing rental");
        }

            if (startDatumOudeRental.isBefore(startDatumNieuweRental)) {
                if(endDatumOudeRental.isAfter(startDatumNieuweRental)) {
                    throw new RentalServiceException("Start and end date",
                    "The rental cannot overlap with another existing rental");
                }
            }

            if (startDatumOudeRental.isAfter(startDatumNieuweRental)) {
                if(startDatumOudeRental.isBefore(endDatumNieuweRental)) {
                    throw new RentalServiceException("Start and end date",
                    "The rental cannot overlap with another existing rental");
                }
            }
        }

        rental.setCar(car);
        rentalRepository.save(rental);
        return rental;
    }

    public void deleteRental(Long id) {
        rentalRepository.deleteById(id);
    }

    public Rental getRental(Long id) {
        return rentalRepository.findById(id).orElse(null);
    }

    public Map<Long, Rental> addRentalsToCars(Map<Long, Rental> rentalData)
            throws RentalServiceException, CarServiceException {
        Map<Long, Rental> addedRentals = new HashMap<>();
        for (Map.Entry<Long, Rental> entry : rentalData.entrySet()) {
            Long carId = entry.getKey();
            Rental rental = entry.getValue();
            
            Car car = carService.getCar(carId);
            if (car == null) {
                throw new CarServiceException("carId", "Car with id " + carId + " does not exist");
            }
            
            if (rental.getStartDateTime().isAfter(rental.getEndDateTime())) {
                throw new RentalServiceException("Start date",
                        "Start date must be before the end date");
            }
            rental.setCar(car);
            rentalRepository.save(rental);
            addedRentals.put(carId, rental);
        }
        System.out.println("Added Rentals successfully");
        return addedRentals;
    }
}
