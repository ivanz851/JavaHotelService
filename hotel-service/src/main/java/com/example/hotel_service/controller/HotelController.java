package com.example.hotel_service.controller;

import com.example.hotel_service.dto.*;
import com.example.hotel_service.service.HotelService;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    public PaginatedResponse<HotelResponse> getAllHotels(@PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<HotelResponse> hotelsPage = hotelService.getAllHotels(pageable);
        return new PaginatedResponse<>(hotelsPage);
    }

    @PostMapping("/{hotelId}/room")
    @ResponseStatus(HttpStatus.CREATED)
    public String addRoom(@PathVariable Long hotelId,
                                 @RequestBody RoomCreateRequest request) {
        hotelService.addRoom(hotelId, request);
        return "Room added to hotel";
    }

    @GetMapping("/{hotelId}/rooms")
    public List<RoomResponse> getRoomsByHotel(@PathVariable Long hotelId) {
        return hotelService.getRoomsByHotel(hotelId);
    }

}
