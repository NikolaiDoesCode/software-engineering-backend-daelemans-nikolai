package com.team17.backend.Greeting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.team17.backend.Greeting.model.Greeting;
import com.team17.backend.Greeting.service.GreetingService;

@CrossOrigin(origins = "*")
@RestController
public class GreetingServiceController {

    @Autowired
    private GreetingService greetingService;

    @GetMapping("/hello")
    public Greeting getAllGreetings() {
        return greetingService.findAllGreetings().get(0);
    }
}
