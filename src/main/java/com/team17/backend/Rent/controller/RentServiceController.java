package com.team17.backend.Rent.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Notification.service.NotificationServiceException;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.Rent.service.RentService;
import com.team17.backend.Rent.service.RentServiceException;
import com.team17.backend.ServiceException.ServiceException;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rent")
public class RentServiceController {

    @Autowired
    private RentService rentService;

    @GetMapping
    public List<Rent> getAllRents() {
        return rentService.findAllRents();
    }

    @GetMapping("/{id}")
    public Rent getRent(@PathVariable @Valid Long id) throws RentServiceException {
        return rentService.getRent(id);
    }

    @GetMapping("email/{email}")
    public Rent getRentByEmail(@PathVariable @Valid String email) throws RentServiceException {
        return rentService.getRentByEmail(email);
    }

    @PostMapping("/addRent/{email}")
    public Rent addRent(@RequestBody @Valid Rent rent, @PathVariable String email)
            throws RentServiceException, UserServiceException, NotificationServiceException, MessagingException {
        return rentService.addRent(rent, email);
    }

    @DeleteMapping("/{id}")
    public void deleteRent(@PathVariable @Valid Long id) throws RentServiceException {
        rentService.deleteRent(id);
    }

    @PutMapping("/{id}/{fuel}/{mileage}/checkIn")
    public Rent checkIn(@PathVariable @Valid Long id, @PathVariable @Valid int fuel,
            @PathVariable @Valid int mileage) throws RentServiceException {
        return rentService.checkIn(id, fuel, mileage);
    }

    @PutMapping("/{id}/{fuel}/{mileage}/checkOut")
    public Rent checkOut(@PathVariable @Valid Long id, @PathVariable @Valid int fuel,
            @PathVariable @Valid int mileage) throws RentServiceException {
        return rentService.checkOut(id, fuel, mileage);
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

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({ ServiceException.class })
    public Map<String, String> handleServiceExceptions(ServiceException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("field", ex.getField());
        errorResponse.put("message", ex.getMessage());
        return errorResponse;
    }

}
