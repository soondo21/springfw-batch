package com.spring;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableBatchProcessing
@SpringBootApplication
public class SpringfwBatchQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringfwBatchQuartzApplication.class, args);
	}

}
