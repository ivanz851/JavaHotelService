package com.example.hotel_service.repository;

import com.example.hotel_service.dto.HotelResponse;
import com.example.hotel_service.model.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
