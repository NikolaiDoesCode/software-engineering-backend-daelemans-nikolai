package com.team17.backend.Notification.controller;

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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Car.model.Car;
import com.team17.backend.Notification.model.Notification;
import com.team17.backend.Notification.service.NotificationService;
import com.team17.backend.Notification.service.NotificationServiceException;
import com.team17.backend.Rent.model.Rent;
import com.team17.backend.ServiceException.ServiceException;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/notification")
public class NotificationServiceController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<Notification> getAllNotifications() {
        return notificationService.findAllNotifications();
    }

    @PostMapping("/{userEmail}")
    public Notification addNotificationToUser(@PathVariable("userEmail") String userEmail,
            @RequestBody @Valid Notification notification) throws NotificationServiceException, UserServiceException {
        return notificationService.addNotificationToUser(userEmail, notification);
    }

    @DeleteMapping("/{notificationId}")
    public Long removeNotification(@Valid @PathVariable("notificationId") long id) {
        return notificationService.removeNotification(id);
    }

    @PutMapping("/{notificationId}")
    public Notification updateStatusNotification(@PathVariable("notificationId") long id)
            throws NotificationServiceException {
        return notificationService.updateStatusNotification(id);
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
