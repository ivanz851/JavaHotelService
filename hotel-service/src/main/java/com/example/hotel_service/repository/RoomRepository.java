package com.example.hotel_service.repository;

import com.example.hotel_service.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
