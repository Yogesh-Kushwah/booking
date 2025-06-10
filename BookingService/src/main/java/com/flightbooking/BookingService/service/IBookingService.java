package com.flightbooking.BookingService.service;

import com.flightbooking.BookingService.model.BookingEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface IBookingService {

   void createBooking(BookingEntity booking); // Create Booking

    List<BookingEntity> getAllBookings(); // Get All Bookings

    List<String> getAllBookingID();

    BookingEntity getBookingById(String id);

    BookingEntity updateBooking(String id, BookingEntity updatedBooking);

    void deleteBooking(String id); // Delete Booking
}
