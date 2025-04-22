package com.example.hotel_service.service;

import com.example.hotel_service.dto.RoomFilterRequest;
import com.example.hotel_service.dto.RoomResponse;
import com.example.hotel_service.model.Room;
import com.example.hotel_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    public List<RoomResponse> filterRooms(RoomFilterRequest filter) {
        List<Room> rooms = roomRepository.findAllByFilter(
                filter.getMinPrice(),
                filter.getMaxPrice(),
                filter.getMinCapacity(),
                filter.getMaxCapacity()
        );

        return rooms.stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getDescription(),
                        room.getCapacity(),
                        room.getPricePerNight(),
                        room.getHotel().getId()
                ))
                .collect(Collectors.toList());
    }
}