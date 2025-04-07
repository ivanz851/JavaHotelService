package com.example.hotel_service.dto;

public record RoomCreateRequest(String description,
                                Integer capacity,
                                Integer pricePerNight
) {}
