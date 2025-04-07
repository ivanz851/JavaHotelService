package com.example.hotel_service.controller;

import com.example.hotel_service.dto.HotelCreateRequest;
import com.example.hotel_service.dto.HotelResponse;
import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotel")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createHotel(@RequestBody HotelCreateRequest hotelCreateRequest) {
        hotelService.CreateHotel(hotelCreateRequest);
        return "Hotel created";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<HotelResponse> getAllHotels() {
        return hotelService.getAllHotels();
    }
}
