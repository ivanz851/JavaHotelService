package com.example.hotel_service.dto;

import java.math.BigDecimal;

public record RoomCreateRequest(String description,
                                Integer capacity,
                                BigDecimal pricePerNight
) {}
