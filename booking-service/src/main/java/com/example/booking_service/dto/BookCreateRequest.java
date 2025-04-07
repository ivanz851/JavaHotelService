package com.example.booking_service.dto;

public record BookCreateRequest(Long hotelId, Long roomId, Long userId) {
}
