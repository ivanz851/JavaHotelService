package com.example.hotel_service.controller;

import com.example.hotel_service.dto.*;
import com.example.hotel_service.service.HotelService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.bean.override.mockito.MockitoBean;

@WebMvcTest(controllers = HotelController.class)
class HotelControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean
    private HotelService hotelService;

    @Test
    @DisplayName("POST /api/hotel – создание отеля")
    void createHotel_returnsCreated() throws Exception {
        HotelCreateRequest body =
                new HotelCreateRequest(null, "Hilton", "1 Street", "NYC",
                        "USA", "+1 000-000", "mail@hilton.com");

        mockMvc.perform(post("/api/hotel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Hotel created"));

        verify(hotelService).CreateHotel(body);
    }

    @Test
    @DisplayName("GET /api/hotel – пагинированный список")
    void getAllHotels_returnsPage() throws Exception {
        HotelResponse dto = new HotelResponse(1L, "Hilton", "1 Street", "NYC",
                "USA", "+1 000-000", "mail@hilton.com");
        Page<HotelResponse> page =
                new PageImpl<>(List.of(dto), PageRequest.of(0, 10), 1);

        when(hotelService.getAllHotels(any())).thenReturn(page);

        mockMvc.perform(get("/api/hotel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name").value("Hilton"))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.last").value(true));

        verify(hotelService).getAllHotels(any());
    }

    @Test
    @DisplayName("POST /api/hotel/{id}/room – добавление номера")
    void addRoom_returnsCreated() throws Exception {
        long hotelId = 42L;
        RoomCreateRequest body =
                new RoomCreateRequest("Deluxe", 2, new BigDecimal("99.99"));

        mockMvc.perform(post("/api/hotel/{hotelId}/room", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Room added to hotel"));

        verify(hotelService).addRoom(eq(hotelId), eq(body));
    }

    @Test
    @DisplayName("GET /api/hotel/{id}/rooms – список номеров")
    void getRoomsByHotel_returnsList() throws Exception {
        long hotelId = 99L;
        List<RoomResponse> rooms = List.of(
                new RoomResponse(1L, "Std",    2, new BigDecimal("50"), hotelId),
                new RoomResponse(2L, "Deluxe", 3, new BigDecimal("90"), hotelId)
        );
        when(hotelService.getRoomsByHotel(hotelId)).thenReturn(rooms);

        mockMvc.perform(get("/api/hotel/{hotelId}/rooms", hotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].capacity").value(2));

        verify(hotelService).getRoomsByHotel(hotelId);
    }
}
