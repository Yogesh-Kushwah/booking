package com.flightbooking.BookingService.service.impl;

import com.flightbooking.BookingService.model.BookingEntity;
import com.flightbooking.BookingService.repository.BookingRepository;
import com.flightbooking.BookingService.service.IBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements IBookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public void createBooking(BookingEntity booking) {
        bookingRepository.save(booking);
    }


    @Override
    public List<BookingEntity> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Override
    public List<String> getAllBookingID() {
        return bookingRepository.findAll()
                .stream()
                .map(BookingEntity::getBookingId)
                .collect(Collectors.toList());
    }



    @Override
    public BookingEntity getBookingById(String id) {
        return bookingRepository.findById(id).orElseThrow(()->new RuntimeException("User Id not found"));
    }


    @Override
    public BookingEntity updateBooking(String id, BookingEntity updatedBooking) {
        return bookingRepository.findById(id).map(booking -> {
            // Update only age and address if provided
            if (updatedBooking.getAge() > 0) {
                booking.setAge(updatedBooking.getAge());
            }
            if (updatedBooking.getAddress() != null && !updatedBooking.getAddress().isEmpty()) {
                booking.setAddress(updatedBooking.getAddress());
            }
            // Update email if provided
            if (updatedBooking.getEmail() != null && !updatedBooking.getEmail().isEmpty()) {
                booking.setEmail(updatedBooking.getEmail());
            }

            // Save and return the updated booking
            return bookingRepository.save(booking);
        }).orElseThrow(() -> new RuntimeException("Booking not found with ID: " + id));
    }



    @Override
    public void deleteBooking(@PathVariable String id) {
        bookingRepository.deleteById(id);
    }
}
