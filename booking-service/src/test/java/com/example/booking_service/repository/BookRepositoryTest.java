package com.example.booking_service.repository;

import com.example.booking_service.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BookRepositoryTest {

    @Autowired
    private BookRepository repository;

    @Test
    @DisplayName("findByUserId возвращает только записи выбранного пользователя")
    void findByUserId_returnsOnlyUserBookings() {

        Book b1 = new Book(null, 1L, 101L, 555L);
        Book b2 = new Book(null, 1L, 102L, 555L);
        Book b3 = new Book(null, 2L, 201L, 777L);
        repository.saveAll(List.of(b1, b2, b3));

        List<Book> user555 = repository.findByUserId(555L);

        assertThat(user555)
                .hasSize(2)
                .allSatisfy(b -> assertThat(b.getUserId()).isEqualTo(555L))
                .extracting(Book::getRoomId)
                .containsExactlyInAnyOrder(101L, 102L);
    }
}
