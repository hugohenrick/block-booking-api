package com.hugo.blockbookingapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class BlockBookingApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockBookingApiApplication.class, args);
	}

}
