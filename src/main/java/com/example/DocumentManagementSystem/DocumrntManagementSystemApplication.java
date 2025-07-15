package com.example.DocumentManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")


public class 	DocumrntManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(DocumrntManagementSystemApplication.class, args);
	}

}
