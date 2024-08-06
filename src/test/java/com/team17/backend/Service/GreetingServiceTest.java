package com.team17.backend.Service;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import com.team17.backend.Greeting.model.Greeting;
import com.team17.backend.Greeting.service.GreetingRepository;
import com.team17.backend.Greeting.service.GreetingService;


@ExtendWith(MockitoExtension.class)
public class GreetingServiceTest {

    @Mock
    GreetingRepository greetingRepository;

    @InjectMocks
    GreetingService greetingService;

    private Greeting greeting1;
    private Greeting greeting2;

    private String validMessage1 = "HELLOOO";
    private String validMessage2 = "Hello world";

  


    @BeforeEach
    public void setUp() { //later mogelijks toetoevoegen hier
        greeting1 = new Greeting(validMessage1);
        greeting2 = new Greeting(validMessage2);
    }

    @Test
    public void givenExistingGreeting_whenFindingAllGreetings_thenListOfGreetingsIsReturned() {
        // Given
        List<Greeting> greetings = new ArrayList<>();
        greetings.add(greeting1);
        greetings.add(greeting2);
        when(greetingService.findAllGreetings()).thenReturn(greetings);

        // When
        List<Greeting> foundGreetings = greetingService.findAllGreetings();

        // Then
        assertEquals(2, foundGreetings.size());
        assertEquals(greeting1, foundGreetings.get(0));
        assertEquals(greeting2, foundGreetings.get(1));
    }




}
