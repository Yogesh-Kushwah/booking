package com.flightbooking.BookingService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Email {

    private String to;
    private String subject;
    private String body;


}
