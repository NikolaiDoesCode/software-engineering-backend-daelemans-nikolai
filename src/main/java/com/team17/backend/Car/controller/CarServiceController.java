package com.team17.backend.Car.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Car.service.CarService;
import com.team17.backend.Car.service.CarServiceException;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.Rental.service.RentalServiceException;
import com.team17.backend.ServiceException.ServiceException;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestParam;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/car")
public class CarServiceController {

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.findAllCars();
    }

    @GetMapping("/{carId}")
    public Car getCar(@Valid @PathVariable("carId") long id) {
        return carService.getCar(id);
    }

    @PostMapping("/{userEmail}")
    public Car addCar(@PathVariable("userEmail") String userEmail, @Valid @RequestBody Car car)
            throws CarServiceException, UserServiceException {
        return carService.addCar(car, userEmail);
    }

    @PostMapping("/cars/{userEmail}")
    public Car[] postMethodName(@PathVariable("userEmail") String userEmail, @Valid @RequestBody Car[] cars)
            throws CarServiceException, UserServiceException {
        return carService.addCars(cars, userEmail);
    }

    @DeleteMapping("/{carId}")
    public Car removeCar(@Valid @PathVariable("carId") long id) throws CarServiceException {
        return carService.removeCar(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ MethodArgumentNotValidException.class })
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put("field", fieldName);
            errors.put("message", errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public ResponseEntity<Map<String, String>> handleServiceExceptions(ServiceException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("field", ex.getField());
        errorResponse.put("message", ex.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
