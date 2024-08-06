package com.team17.backend.Mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.team17.backend.Mail.model.EmailRequest;
import com.team17.backend.Mail.service.MailService;

import javax.mail.MessagingException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public String sendEmail(
            @RequestBody EmailRequest emailRequest) {
        try {
            mailService.sendEmail(emailRequest);
            return "Email sent successfully";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error while sending email: " + e.getMessage();
        }
    }
}
