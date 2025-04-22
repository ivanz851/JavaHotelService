package com.example.hotel_service.service;

import com.example.hotel_service.dto.HotelCreateRequest;
import com.example.hotel_service.dto.HotelResponse;
import com.example.hotel_service.dto.RoomCreateRequest;
import com.example.hotel_service.dto.RoomResponse;
import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.model.Room;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

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


    public Page<HotelResponse> getAllHotels(Pageable pageable) {
        return hotelRepository.findAll(pageable)
                .map(hotel -> new HotelResponse(
                        hotel.getId(),
                        hotel.getName(),
                        hotel.getAddress(),
                        hotel.getCity(),
                        hotel.getCountry(),
                        hotel.getPhone(),
                        hotel.getEmail()
                ));
    }


    public void addRoom(Long hotelId, RoomCreateRequest request) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        Room room = new Room();
        room.setDescription(request.description());
        room.setCapacity(request.capacity());
        room.setPricePerNight(request.pricePerNight());
        room.setHotel(hotel);

        roomRepository.save(room);
    }

    public List<RoomResponse> getRoomsByHotel(Long hotelId) {
        Hotel hotel = hotelRepository.findById(hotelId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Hotel not found"));

        return hotel.getRooms().stream()
                .map(room -> new RoomResponse(
                        room.getId(),
                        room.getDescription(),
                        room.getCapacity(),
                        room.getPricePerNight(),
                        hotel.getId()
                ))
                .toList();
    }

}
