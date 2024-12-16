package com.ainkai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EcomapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcomapiApplication.class, args);
	}

}
