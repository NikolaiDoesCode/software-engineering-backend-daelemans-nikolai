package com.team17.backend.Groupchat.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Groupchat.model.Groupchat;
import com.team17.backend.Groupchat.service.GroupchatService;
import com.team17.backend.Groupchat.service.GroupchatServiceException;
import com.team17.backend.ServiceException.ServiceException;
import com.team17.backend.User.model.User;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/groupchat")
public class GroupchatServiceController {

    @Autowired
    private GroupchatService groupchatService;

    @GetMapping()
    public List<Groupchat> getAllGroupchats() {
        return groupchatService.getAllGroupchats();
    }

    @PostMapping()
    public Groupchat createGroupchat() throws UserServiceException {
        return groupchatService.createGroupchat();
    }

    @GetMapping("/users/{id}")
    public List<User> getUsersOfGroupChat(@PathVariable @Valid long id) throws GroupchatServiceException {
        return groupchatService.getUsersOfGroupChat(id);
    }

    @PostMapping("/add")
    public Groupchat addUserToGroupChat(@RequestParam long id, @RequestParam String email)
            throws GroupchatServiceException, UserServiceException {
        return groupchatService.addUserToGroupChat(id, email);
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
