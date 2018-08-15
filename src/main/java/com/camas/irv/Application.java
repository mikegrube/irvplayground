package com.camas.irv;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}

//Invocation for development:
//	gradle -Dspring.profiles.active=dev bootRun

//Installation as a Linux service:
//	https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html

