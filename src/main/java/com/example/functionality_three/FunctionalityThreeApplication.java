package com.example.functionality_three;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "com.example.functionality_three.repositories")
@ComponentScan({"com.example.functionality_three.repositories", "com.example.functionality_three.controllers",
        "com.example.functionality_three.DTOs", "com.example.functionality_three.entities",
        "com.example.functionality_three.services"})
public class FunctionalityThreeApplication extends SpringBootServletInitializer {


	public static void main(String[] args) {
		SpringApplication.run(FunctionalityThreeApplication.class, args);
		System.out.println("running!");
	}

}
