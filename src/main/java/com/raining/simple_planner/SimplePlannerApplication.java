package com.raining.simple_planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SimplePlannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplePlannerApplication.class, args);
	}

}
