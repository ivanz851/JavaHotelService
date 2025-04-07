package com.example.hotel_service.service;

import com.example.hotel_service.dto.HotelCreateRequest;
import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    public void CreateHotel(HotelCreateRequest hotelCreateRequest) {
        Hotel hotel = new Hotel();
        hotel.setName(hotelCreateRequest.name());
        hotel.setAddress(hotelCreateRequest.address());
        hotel.setCity(hotelCreateRequest.city());
        hotel.setCountry(hotelCreateRequest.country());
        hotel.setPhone(hotelCreateRequest.phone());
        hotel.setEmail(hotelCreateRequest.email());
        hotelRepository.save(hotel);
    }

}
