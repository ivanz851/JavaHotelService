package com.example.hotel_service.repository;

import com.example.hotel_service.model.Hotel;
import com.example.hotel_service.model.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class HotelRepositoryTest {

    @Autowired TestEntityManager em;
    @Autowired HotelRepository hotelRepository;
    @Autowired RoomRepository roomRepository;

    @Test
    @DisplayName("Сохраняет отель и находит по id")
    void saveAndFindHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("Hilton");
        hotel.setAddress("1 Street"); hotel.setCity("NYC");
        hotel.setCountry("USA");       hotel.setPhone("000");
        hotel.setEmail("mail@hilton.com");

        Hotel saved = hotelRepository.save(hotel);

        Optional<Hotel> found = hotelRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Hilton");
    }

    @Test
    @DisplayName("Номера сохраняются вместе с отелем")
    void cascadeSaveRooms() {
        Hotel hotel = new Hotel();
        hotel.setName("Hilton");

        Room room = new Room(null, "Std", 2,
                new BigDecimal("50"), hotel);
        hotel.setRooms(List.of(room));

        Hotel saved = hotelRepository.save(hotel);
        em.flush();

        assertThat(room.getId()).isNotNull();

        Room persisted = roomRepository.findById(room.getId()).orElseThrow();
        assertThat(persisted.getHotel().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("Удаление комнаты из списка удаляет запись в БД")
    void orphanRemovalDeletesRoom() {
        Hotel hotel = new Hotel();
        hotel.setName("Hilton");

        Room r1 = new Room(null, "Std",    2, new BigDecimal("50"), hotel);
        Room r2 = new Room(null, "Deluxe", 3, new BigDecimal("90"), hotel);
        hotel.setRooms(new ArrayList<>(List.of(r1, r2)));

        hotel = hotelRepository.save(hotel);
        em.flush();

        hotel.getRooms().remove(r1);
        hotelRepository.save(hotel);
        em.flush();

        assertThat(roomRepository.findById(r1.getId())).isEmpty();
        assertThat(roomRepository.findById(r2.getId())).isPresent();
    }
}
