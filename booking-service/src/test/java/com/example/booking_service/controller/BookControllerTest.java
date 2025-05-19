package com.example.booking_service.controller;

import com.example.booking_service.dto.BookCreateRequest;
import com.example.booking_service.dto.BookUserResponse;
import com.example.booking_service.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @TestConfiguration
    static class Config {
        @Bean
        BookService bookService() {
            return mock(BookService.class);
        }
    }

    @Autowired
    private BookService bookService;

    @Test
    @DisplayName("POST /api/book – возвращает 201 и передаёт запрос в сервис")
    void createBook_returnsCreated() throws Exception {
        BookCreateRequest body = new BookCreateRequest(1L, 101L, 555L);

        mockMvc.perform(post("/api/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(content().string("Booking created"));

        ArgumentCaptor<BookCreateRequest> captor = ArgumentCaptor.forClass(BookCreateRequest.class);
        verify(bookService).CreateBook(captor.capture());
        assertThat(captor.getValue()).isEqualTo(body);
    }

    @Test
    @DisplayName("GET /api/book/user/{id} – возвращает список бронирований")
    void getBookByUserId_returnsList() throws Exception {
        List<BookUserResponse> stub = List.of(new BookUserResponse(1L, 101L));
        when(bookService.GetUserBook(555L)).thenReturn(stub);

        mockMvc.perform(get("/api/book/user/{id}", 555L))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(stub)));

        verify(bookService).GetUserBook(555L);
    }
}
