package com.example.hotel_service.controller;

import com.example.hotel_service.dto.RoomFilterRequest;
import com.example.hotel_service.dto.RoomResponse;
import com.example.hotel_service.service.RoomService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = RoomController.class,
        properties = "spring.flyway.enabled=false"
)
class RoomControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper mapper;

    @MockitoBean
    private RoomService roomService;

    @Test
    @DisplayName("POST /api/rooms/filter – фильтр номеров")
    void filterRooms_returnsFilteredList() throws Exception {
        RoomFilterRequest filter = new RoomFilterRequest();
        filter.setMinCapacity(1);
        filter.setMaxCapacity(3);
        filter.setMaxPrice(new BigDecimal("150"));

        List<RoomResponse> result = List.of(
                new RoomResponse(10L, "Std", 2, new BigDecimal("120"), 5L)
        );
        when(roomService.filterRooms(filter)).thenReturn(result);

        mockMvc.perform(post("/api/rooms/filter")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(filter)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(10));

        verify(roomService).filterRooms(filter);
    }
}
