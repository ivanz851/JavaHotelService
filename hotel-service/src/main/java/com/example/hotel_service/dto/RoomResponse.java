package com.example.hotel_service.dto;

import java.math.BigDecimal;

public record RoomResponse(
        Long id,
        String description,
        Integer capacity,
        BigDecimal pricePerNight,
        Long hotelId
) {}
