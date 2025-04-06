package com.example.hotel_service.controller;

import com.example.hotel_service.dto.HotelCreateRequest;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/create-hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelRepository hotelRepository;
    private final HotelService hotelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createHotel(@RequestBody HotelCreateRequest hotelCreateRequest) {
        hotelService.CreateHotel(hotelCreateRequest);
        return "Hotel created";
    }
}
