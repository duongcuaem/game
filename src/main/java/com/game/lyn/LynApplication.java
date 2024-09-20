package com.game.lyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class LynApplication {

	public static void main(String[] args) {
		SpringApplication.run(LynApplication.class, args);
	}

}
