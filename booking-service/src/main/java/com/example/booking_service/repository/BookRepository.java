package com.example.booking_service.repository;

import com.example.booking_service.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByUserId(Long userId);
}
