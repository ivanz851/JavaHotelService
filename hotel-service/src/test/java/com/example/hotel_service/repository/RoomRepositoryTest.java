package com.example.hotel_service.repository;

import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.model.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomRepositoryTest {

    @Autowired RoomRepository roomRepository;
    @Autowired HotelRepository hotelRepository;

    private Room cheapSmall;
    private Room midStd;
    private Room deluxeLarge;

    @BeforeEach
    void setUp() {
        Hotel hotel = new Hotel();
        hotel.setName("Hilton");
        hotel.setAddress("1 Street");
        hotel.setCity("NYC");
        hotel.setCountry("USA");
        hotel.setPhone("000");
        hotel.setEmail("mail@hilton.com");
        hotel = hotelRepository.save(hotel);

        cheapSmall = roomRepository.save(
                new Room(null, "Cheap", 1, new BigDecimal("40"), hotel));
        midStd = roomRepository.save(
                new Room(null, "Std",   2, new BigDecimal("80"), hotel));
        deluxeLarge = roomRepository.save(
                new Room(null, "Lux",   4, new BigDecimal("180"), hotel));
    }

    @Nested
    @DisplayName("Проверка границ цены")
    class PriceFilter {

        @Test
        @DisplayName("minPrice = 50 вернёт только номера ценой >= 50")
        void minPrice() {
            List<Room> res = roomRepository.findAllByFilter(
                    new BigDecimal("50"), null, null, null);

            assertThat(res).containsExactlyInAnyOrder(midStd, deluxeLarge)
                    .doesNotContain(cheapSmall);
        }

        @Test
        @DisplayName("maxPrice = 100 вернёт только номера ценой <= 100")
        void maxPrice() {
            List<Room> res = roomRepository.findAllByFilter(
                    null, new BigDecimal("100"), null, null);

            assertThat(res).containsExactlyInAnyOrder(cheapSmall, midStd)
                    .doesNotContain(deluxeLarge);
        }

        @Test
        @DisplayName("Диапазон 50 – 150")
        void priceRange() {
            List<Room> res = roomRepository.findAllByFilter(
                    new BigDecimal("50"), new BigDecimal("150"), null, null);

            assertThat(res).containsExactly(midStd);
        }
    }

    @Nested
    @DisplayName("Проверка вместимости")
    class CapacityFilter {

        @Test
        @DisplayName("minCapacity = 3 вернёт номера в которых  >= 3 мест")
        void minCapacity() {
            List<Room> res = roomRepository.findAllByFilter(
                    null, null, 3, null);

            assertThat(res).containsExactly(deluxeLarge);
        }

        @Test
        @DisplayName("maxCapacity = 2 вернёт номера в которых <= 2 мест")
        void maxCapacity() {
            List<Room> res = roomRepository.findAllByFilter(
                    null, null, null, 2);

            assertThat(res).containsExactlyInAnyOrder(cheapSmall, midStd);
        }

        @Test
        @DisplayName("Диапазон 2 – 3 места")
        void capacityRange() {
            List<Room> res = roomRepository.findAllByFilter(
                    null, null, 2, 3);

            assertThat(res).containsExactly(midStd);
        }
    }

    @Test
    @DisplayName("Все параметры = null - возвращает все записи")
    void allNull_returnsAll() {
        List<Room> res = roomRepository.findAllByFilter(
                null, null, null, null);

        assertThat(res).containsExactlyInAnyOrder(cheapSmall, midStd, deluxeLarge);
    }
}
