package com.example.notification_service.book.event;

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
