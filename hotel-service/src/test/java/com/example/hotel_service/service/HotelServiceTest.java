package com.example.hotel_service.service;

import com.example.hotel_service.dto.*;
import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.model.Room;
import com.example.hotel_service.repository.HotelRepository;
import com.example.hotel_service.repository.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class HotelServiceTest {

    @Mock  private HotelRepository hotelRepository;
    @Mock  private RoomRepository  roomRepository;
    @InjectMocks private HotelService hotelService;

    HotelServiceTest() { MockitoAnnotations.openMocks(this); }

    @Test
    @DisplayName("createHotel – сохраняет отель")
    void createHotel() {
        HotelCreateRequest req = new HotelCreateRequest(
                null, "Hilton", "1 St", "NY", "USA", "000", "mail@h.com");

        ArgumentCaptor<Hotel> captor = ArgumentCaptor.forClass(Hotel.class);

        hotelService.CreateHotel(req);
        verify(hotelRepository).save(captor.capture());

        Hotel saved = captor.getValue();
        assertThat(saved.getName()).isEqualTo("Hilton");
        assertThat(saved.getCity()).isEqualTo("NY");
    }

    @Test
    @DisplayName("getAllHotels – маппит Page<Hotel> -> Page<HotelResponse>")
    void getAllHotels() {
        Hotel h = new Hotel(); h.setId(1L); h.setName("Hilton");
        Page<Hotel> page = new PageImpl<>(List.of(h), PageRequest.of(0,10), 1);

        when(hotelRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<HotelResponse> resp = hotelService.getAllHotels(PageRequest.of(0,10));

        assertThat(resp.getTotalElements()).isEqualTo(1);
        assertThat(resp.getContent().get(0).name()).isEqualTo("Hilton");
    }

    @Test
    @DisplayName("addRoom")
    void addRoom() {
        long hotelId = 77L;
        Hotel hotel = new Hotel(); hotel.setId(hotelId);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        RoomCreateRequest req = new RoomCreateRequest("Std", 2, new BigDecimal("50"));

        hotelService.addRoom(hotelId, req);

        ArgumentCaptor<Room> captor = ArgumentCaptor.forClass(Room.class);
        verify(roomRepository).save(captor.capture());

        Room saved = captor.getValue();
        assertThat(saved.getHotel()).isSameAs(hotel);
        assertThat(saved.getCapacity()).isEqualTo(2);
    }

    @Test
    @DisplayName("addRoom – hotel NOT FOUND -> 404")
    void addRoom_hotelNotFound() {
        when(hotelRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                hotelService.addRoom(1L, new RoomCreateRequest("x",1,BigDecimal.ONE)))
                .hasMessageContaining("Hotel not found");
    }

    @Test
    @DisplayName("getRoomsByHotel – возвращает DTO-список")
    void getRoomsByHotel() {
        Hotel hotel = new Hotel(); hotel.setId(5L);
        Room r = new Room(10L,"Std",2,new BigDecimal("50"), hotel);
        hotel.setRooms(List.of(r));

        when(hotelRepository.findById(5L)).thenReturn(Optional.of(hotel));

        List<RoomResponse> list = hotelService.getRoomsByHotel(5L);

        assertThat(list).singleElement()
                .matches(dto -> dto.id()==10L && dto.hotelId()==5L);
    }
}
