package com.autoever.carstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class CarstoreApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarstoreApplication.class, args);
	}

}
