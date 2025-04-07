package com.example.hotel_service.dto;

public record RoomResponse(
        Long id,
        String description,
        Integer capacity,
        Integer pricePerNight,
        Long hotelId
) {}
