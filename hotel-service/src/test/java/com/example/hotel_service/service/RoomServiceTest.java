package com.example.hotel_service.service;

import com.example.hotel_service.dto.RoomFilterRequest;
import com.example.hotel_service.dto.RoomResponse;
import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.model.Room;
import com.example.hotel_service.repository.RoomRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RoomServiceTest {

    @Mock  private RoomRepository roomRepository;
    @InjectMocks private RoomService roomService;

    RoomServiceTest() { MockitoAnnotations.openMocks(this); }

    @Test
    @DisplayName("filterRooms – пробрасывает параметры и маппит DTO")
    void filterRooms() {
        Hotel h = new Hotel(); h.setId(3L);
        Room room = new Room(8L,"Lux",4,new BigDecimal("150"), h);

        RoomFilterRequest f = new RoomFilterRequest();
        f.setMaxPrice(new BigDecimal("200"));

        when(roomRepository.findAllByFilter(
                isNull(), eq(new BigDecimal("200")),
                isNull(), isNull()))
                .thenReturn(List.of(room));

        List<RoomResponse> res = roomService.filterRooms(f);

        assertThat(res).singleElement()
                .matches(dto -> dto.pricePerNight().compareTo(BigDecimal.valueOf(150))==0
                        && dto.hotelId()==3L);
    }
}
