package com.team17.backend.Rental.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.service.CarServiceException;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.service.RentalService;
import com.team17.backend.Rental.service.RentalServiceException;
import com.team17.backend.ServiceException.ServiceException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rental")
public class RentalServiceController {

    @Autowired
    private RentalService rentalService;

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.findAllRentalsWithoutRentedOnes();
    }

    @GetMapping("/rented")
    public List<Rental> getAllRentalsWithRentend() {
        return rentalService.findAllRentalsWithRentedOnes();
    }

    @GetMapping("/search")
    public List<Rental> searchRentals(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String brand)
            throws RentalServiceException {
        return rentalService.searchRentals(email, startDate, endDate, brand);
    }

    @GetMapping("/{id}")
    public Rental getRental(@Valid @PathVariable("id") long id) throws RentalServiceException {
        return rentalService.getRental(id);
    }

    // @GetMapping("/carOfRental/{rentalId}")
    // public Car getCarOfRental(@Valid @PathVariable("rentalId") long id) {
    // return rentalService.findCarOfRental(id);
    // }

    @PostMapping("/{carId}/addRental")
    public Rental addRentalToCar(@PathVariable("carId") long id, @Valid @RequestBody Rental rental)
            throws RentalServiceException, CarServiceException {
        return rentalService.addRentalToCar(id, rental);
    }

    @PostMapping("/addRentals")
    public Map<Long, Rental> addRentalsToCars(@Valid @RequestBody Map<Long, Rental> rentalData)
            throws RentalServiceException, CarServiceException {
        return rentalService.addRentalsToCars(rentalData);
    }
   
    @DeleteMapping("/{rentalId}")
    public void deleteRental(@Valid @PathVariable("rentalId") long id) {
        rentalService.deleteRental(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            MethodArgumentNotValidException.class })
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler({ RentalServiceException.class })
    // public Map<String, String> handleServiceExceptions(RentalServiceException ex)
    // {
    // Map<String, String> errors = new HashMap<>();
    // errors.put(ex.getField(), ex.getMessage());
    // return errors;
    // }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceExceptions(ServiceException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("field", ex.getField());
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }
}
