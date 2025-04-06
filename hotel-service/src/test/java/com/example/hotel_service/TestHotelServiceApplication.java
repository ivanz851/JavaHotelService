package com.example.hotel_service;

import org.springframework.boot.SpringApplication;

public class TestHotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.from(HotelServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
