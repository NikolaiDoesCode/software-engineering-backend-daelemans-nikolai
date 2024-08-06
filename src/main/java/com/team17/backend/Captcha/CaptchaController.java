package com.team17.backend.Captcha;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    private static final String RECAPTCHA_SECRET = "6LclyfEpAAAAAKQfhPDayfV8lVZ7rKjcIwRX2GOR";

    @PostMapping("/response")
    public ResponseEntity<?> getResponse(@RequestParam("token") String token) {
        System.out.println("Received token: " + token); 
        String url = "https://www.google.com/recaptcha/api/siteverify";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        Map<String, String> body = new HashMap<>();
        body.put("secret", RECAPTCHA_SECRET);
        body.put("response", token);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<RecaptchaResponse> recaptchaResponse = restTemplate.postForEntity(
                url, request, RecaptchaResponse.class);

        if (recaptchaResponse.getBody() != null && recaptchaResponse.getBody().isSuccess()) {
            return ResponseEntity.ok(recaptchaResponse.getBody());
        } else {
            return ResponseEntity.badRequest().body(recaptchaResponse.getBody());
        }
    }
}