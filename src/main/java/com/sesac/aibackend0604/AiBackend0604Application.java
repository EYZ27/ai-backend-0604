package com.sesac.aibackend0604;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AiBackend0604Application {

	public static void main(String[] args) {

		System.out.println("GOOGLE_CLIENT_ID"+System.getenv("GOOGLE_CLIENT_ID"));

		SpringApplication.run(AiBackend0604Application.class, args);
	}

}
