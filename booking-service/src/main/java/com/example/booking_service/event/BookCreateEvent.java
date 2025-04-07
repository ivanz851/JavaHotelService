package com.example.booking_service.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateEvent {
    private Long userId;
    private Long hotelId;
}
