package com.example.hotel_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoomFilterRequest {
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minCapacity;
    private Integer maxCapacity;
}
