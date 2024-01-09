package com.cookingapp.cookingapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class CookingAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(CookingAppApplication.class, args);
	}

}
