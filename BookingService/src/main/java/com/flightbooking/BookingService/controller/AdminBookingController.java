package com.flightbooking.BookingService.controller;

import com.flightbooking.BookingService.model.BookingEntity;
import com.flightbooking.BookingService.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bookings/admin")
public class AdminBookingController {


    @Autowired
    private IBookingService bookingService; // Use Interface for Loose Coupling


    @GetMapping
    public List<BookingEntity> getAllBookings() {
        return bookingService.getAllBookings();
    }



    @GetMapping("/id")
    public List<String> getAllBookingId() {
        return bookingService.getAllBookingID();
    }

}
