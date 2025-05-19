package com.example.booking_service.service;

import com.example.book.event.BookCreateEvent;
import com.example.booking_service.dto.BookCreateRequest;
import com.example.booking_service.dto.BookUserResponse;
import com.example.booking_service.model.Book;
import com.example.booking_service.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock  private BookRepository bookRepository;
    @Mock  private KafkaTemplate<String, BookCreateEvent> kafkaTemplate;
    @Mock  private GrpcServiceBook grpcServiceBook;

    @InjectMocks
    private BookService bookService;

    private BookCreateRequest request;

    @BeforeEach
    void setUp() {
        request = new BookCreateRequest(1L, 101L, 555L);
    }

    @Test
    void createBook_savesEntityAndPublishesEvent() {
        when(grpcServiceBook.getRoomPrice(1L, 101L)).thenReturn(150F);

        bookService.CreateBook(request);

        ArgumentCaptor<Book> bookCaptor = ArgumentCaptor.forClass(Book.class);
        verify(bookRepository).save(bookCaptor.capture());
        Book saved = bookCaptor.getValue();
        assertThat(saved.getHotelId()).isEqualTo(1L);
        assertThat(saved.getRoomId()).isEqualTo(101L);
        assertThat(saved.getUserId()).isEqualTo(555L);

        ArgumentCaptor<BookCreateEvent> eventCaptor = ArgumentCaptor.forClass(BookCreateEvent.class);
        verify(kafkaTemplate).send(eq("book"), eventCaptor.capture());
        BookCreateEvent event = eventCaptor.getValue();
        assertThat(event.getHotelId()).isEqualTo("test2");
        assertThat(event.getUserId()).isEqualTo("test1");

        verify(grpcServiceBook).getRoomPrice(1L, 101L);
    }

    @Test
    void getUserBook_convertsEntitiesToDto() {
        Book entity = new Book();
        entity.setHotelId(1L);
        entity.setRoomId(101L);
        entity.setUserId(555L);
        when(bookRepository.findByUserId(555L)).thenReturn(List.of(entity));

        List<BookUserResponse> result = bookService.GetUserBook(555L);

        assertThat(result).containsExactly(new BookUserResponse(1L, 101L));
    }
}
