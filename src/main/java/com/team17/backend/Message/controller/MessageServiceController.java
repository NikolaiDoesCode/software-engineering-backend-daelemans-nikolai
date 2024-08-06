package com.team17.backend.Message.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Groupchat.service.GroupchatServiceException;
import com.team17.backend.Message.model.Message;
import com.team17.backend.Message.service.MessageService;
import com.team17.backend.Message.service.MessageServiceException;
import com.team17.backend.ServiceException.ServiceException;
import com.team17.backend.User.service.UserServiceException;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/message")
public class MessageServiceController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<Message> getAllMessages() {
        return messageService.getAllMessages();
    }

    @PostMapping("/{userEmail}/{groupchatId}")
    public Message addMessages(@PathVariable("userEmail") String userEmail,
            @Valid @PathVariable("groupchatId") long groupchatId, @RequestBody Message message)
            throws MessageServiceException, UserServiceException, GroupchatServiceException {
        System.out.println("userEmail");
        System.out.println(userEmail);
        System.out.println("groupchatId");
        System.out.println(groupchatId);

        return messageService.addMessage(message, userEmail, groupchatId);
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
