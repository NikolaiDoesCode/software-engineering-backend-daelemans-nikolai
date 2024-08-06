package com.team17.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

// import com.team17.backend.JacksonConfig;

@SpringBootApplication
@ComponentScan({ "com.team17.backend.config", "com.team17.backend.Car", "com.team17.backend.Greeting",
		"com.team17.backend.Groupchat", "com.team17.backend.Mail", "com.team17.backend.Message",
		"com.team17.backend.Notification", "com.team17.backend.Rent", "com.team17.backend.Rental",
		"com.team17.backend.User", "com.team17.backend.Captcha" })
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
