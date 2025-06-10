package com.flightbooking.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    private String name ;
    private Long amount;
    private Long quantity;
    private String currency;
}



