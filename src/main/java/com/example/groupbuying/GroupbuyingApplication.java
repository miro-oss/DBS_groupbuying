package com.example.groupbuying;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GroupbuyingApplication {

	public static void main(String[] args) {
		SpringApplication.run(GroupbuyingApplication.class, args);
	}
}