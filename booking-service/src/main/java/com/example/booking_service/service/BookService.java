package com.example.booking_service.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.booking_service.repository.BookRepository;
import com.example.booking_service.dto.*;
import com.example.book.event.BookCreateEvent;
import org.springframework.kafka.core.KafkaTemplate;

import com.example.booking_service.model.Book;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final KafkaTemplate<String, BookCreateEvent> kafkaTemplate;


    public void CreateBook(BookCreateRequest bookCreateRequest) {
        Book book = new Book();
        book.setHotelId(bookCreateRequest.hotelId());
        book.setRoomId(bookCreateRequest.roomId());
        book.setUserId(bookCreateRequest.userId());

        GrpcServiceBook grpcServiceBook = new GrpcServiceBook("localhost", 9090);
        float price = grpcServiceBook.getRoomPrice(book.getHotelId(), book.getRoomId());
        System.out.println("Response received");

        bookRepository.save(book);

        BookCreateEvent bookCreateEvent = new BookCreateEvent();
        bookCreateEvent.setUserId("test1");
        bookCreateEvent.setHotelId("test2");
        kafkaTemplate.send("book", bookCreateEvent);

    }

    public List<BookUserResponse> GetUserBook(Long userId) {
        List<Book> books = bookRepository.findByUserId(userId);
        return books.stream()
                .map(book -> new BookUserResponse(book.getHotelId(), book.getRoomId()))
                .collect(Collectors.toList());
    }

}
