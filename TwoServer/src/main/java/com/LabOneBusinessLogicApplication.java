package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication

@EnableJms
public class LabOneBusinessLogicApplication {

	public static void main(String[] args) {
		SpringApplication.run(LabOneBusinessLogicApplication.class, args);
	}

}
