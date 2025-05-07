package com.example.booking_service.controller;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.example.booking_service.service.BookService;
import org.springframework.http.HttpStatus;
import com.example.booking_service.dto.*;
import java.util.List;


@RestController
@RequestMapping("/api/book")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createBook(@RequestBody BookCreateRequest bookCreateRequest) {
        System.out.println(bookCreateRequest);
        bookService.CreateBook(bookCreateRequest);
        return "Booking created";
    }

    @GetMapping("user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public List<BookUserResponse> getBookByUserId(@PathVariable Long userId) {
        return bookService.GetUserBook(userId);
    }
}
