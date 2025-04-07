package com.example.hotel_service.dto;

public record HotelResponse(Long id,
                            String name,
                            String address,
                            String city,
                            String country,
                            String phone,
                            String email) {}