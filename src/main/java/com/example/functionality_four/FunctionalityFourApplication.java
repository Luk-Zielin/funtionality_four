package com.example.functionality_four;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "com.example.functionality_four.repositories")
@ComponentScan({"com.example.functionality_four.repositories", "com.example.functionality_four.controllers",
        "com.example.functionality_four.DTOs", "com.example.functionality_four.entities",
        "com.example.functionality_four.services"})
public class FunctionalityFourApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(FunctionalityFourApplication.class, args);
		System.out.println("running!");
	}

}
