package com.team17.backend.Greeting.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.team17.backend.Greeting.model.Greeting;

@Service
public class GreetingService {

    @Autowired
    private GreetingRepository greetingRepository;

    public GreetingService() {
    }

    public List<Greeting> findAllGreetings() {
        return greetingRepository.findAll();
    }
}
