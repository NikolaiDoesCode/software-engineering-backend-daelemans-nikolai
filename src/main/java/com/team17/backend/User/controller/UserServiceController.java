package com.team17.backend.User.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.Message.model.Message;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.Rental.model.Rental;
import com.team17.backend.ServiceException.ServiceException;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.model.LoginResponse;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserService;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/user")
public class UserServiceController {

    @Autowired
    private UserService userService;

    @GetMapping()
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public User getUser(@PathVariable @Valid String email) throws UserServiceException {
        return userService.getUser(email);
    }

    @PostMapping()
    public User addUser(@RequestBody @Valid User user) throws UserServiceException {
        return userService.addUser(user);
    }

    @PostMapping("/login")
    public LoginResponse logUser(@RequestBody Map<String, String> loginRequest) throws UserServiceException {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");
        return userService.logUser(email, password);
    }

    @PutMapping("/setAdmin/{email}/{isAdmin}")
    public User setAdmin(@PathVariable @Valid String email, @PathVariable @Valid boolean isAdmin)
            throws UserServiceException {
        return userService.setAdmin(email, isAdmin);
    }

    @PutMapping("/setAccountant/{email}/{isAccountant}")
    public User setAccountant(@PathVariable @Valid String email, @PathVariable @Valid boolean isAccountant)
            throws UserServiceException {
        return userService.setAccountant(email, isAccountant);
    }

    @PutMapping("/setRole/{email}/{role}")
    public User setRole(@PathVariable @Valid String email, @PathVariable @Valid String role)
            throws UserServiceException {
        return userService.setRole(email, role);
    }

    @DeleteMapping("/{email}")
    public void deleteUser(@PathVariable @Valid String email) throws UserServiceException {
        userService.deleteUser(email);
    }

    @GetMapping("/cars/{email}")
    public List<Car> getCarsUser(@PathVariable String email) throws UserServiceException {
        return userService.getCarsUser(email);
    }

    @GetMapping("/rents/{email}")
    public List<Rent> getRentsUser(@PathVariable String email) throws UserServiceException {
        return userService.getRentsUser(email);
    }

    @GetMapping("/rentals/{email}")
    public List<Rental> getRentalsUser(@PathVariable String email) throws UserServiceException {
        return userService.getRentalsUser(email);
    }

    @GetMapping("/notifications/{email}")
    public List<Notification> getNotificationsUser(@PathVariable String email) throws UserServiceException {
        return userService.getNotificationsUser(email);
    }

    @GetMapping("/notifications/new/{email}")
    public List<Notification> getNewNotificationsUser(@PathVariable String email) throws UserServiceException {
        return userService.getNewNotificationsUser(email);
    }

    @GetMapping("/messages/{email}")
    public List<Message> getMessagesUser(@PathVariable String email) throws UserServiceException {
        return userService.getMessagesUser(email);
    }

    @GetMapping("/groupchats/{email}")
    public List<Groupchat> getGroupchatsUser(@PathVariable String email) throws UserServiceException {
        return userService.getGroupchatsUser(email);
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
