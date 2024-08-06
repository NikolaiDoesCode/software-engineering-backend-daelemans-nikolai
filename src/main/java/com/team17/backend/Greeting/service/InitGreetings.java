package com.team17.backend.Greeting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.team17.backend.Greeting.model.Greeting;

import jakarta.annotation.PostConstruct;

@Component
public class InitGreetings {

    @Autowired
    private GreetingRepository greetingRepository;

    @PostConstruct
    public void insertGreeting() {
        greetingRepository.save(new Greeting("Hello world!"));
    }
}
