package com.example.hotel_service.controller;

import com.example.hotel_service.dto.RoomFilterRequest;
import com.example.hotel_service.dto.RoomResponse;
import com.example.hotel_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/filter")
    @ResponseStatus(HttpStatus.OK)
    public List<RoomResponse> filterRooms(@RequestBody RoomFilterRequest filter) {
        return roomService.filterRooms(filter);
    }
}
