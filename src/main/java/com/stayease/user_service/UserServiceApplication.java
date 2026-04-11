package com.stayease.user_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class UserServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(UserServiceApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
		logger.info("User Service is up and Running");
		logger.info("Swagger Link: http://localhost:8082/swagger-ui.html");
	}
}
