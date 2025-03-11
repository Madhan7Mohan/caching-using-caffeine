package com.example.caching.using.caffeine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CachingUsingCaffeineApplication {

	public static void main(String[] args) {
		SpringApplication.run(CachingUsingCaffeineApplication.class, args);
	}

}
