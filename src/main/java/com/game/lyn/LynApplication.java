package com.game.lyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")  // Kích hoạt Auditing và chỉ định auditor provider
public class LynApplication {

	public static void main(String[] args) {
		SpringApplication.run(LynApplication.class, args);
	}

}
