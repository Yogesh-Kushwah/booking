package com.flightbooking.BookingService.controller;

import com.flightbooking.BookingService.dto.BookingRequest;
import com.flightbooking.BookingService.dto.Email;
import com.flightbooking.BookingService.dto.StripeResponse;
import com.flightbooking.BookingService.feignclient.NotificationService;
import com.flightbooking.BookingService.feignclient.PaymentGateway;
import com.flightbooking.BookingService.model.BookingEntity;
import com.flightbooking.BookingService.service.IBookingService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings/user")
public class BookingController {

    @Autowired
    private PaymentGateway paymentGateway;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private IBookingService bookingService;

    @PostMapping
    @CircuitBreaker(name = "paymentServiceBreaker", fallbackMethod = "paymentFallback")
    public StripeResponse createBooking(@RequestBody BookingEntity booking) {

        BookingRequest request = new BookingRequest();
        request.setName("Flight-Ticket");
        request.setAmount(booking.getPaymentAmount());
        request.setQuantity(1L);
        request.setCurrency("INR");

        StripeResponse stripe = paymentGateway.checkoutProduct(request);

        if (stripe == null) {
            throw new RuntimeException("Payment service did not respond.");
        }

        if (!"success".equalsIgnoreCase(stripe.getStatus())) {
            return StripeResponse.builder()
                    .status("Payment Failed")
                    .sessionId("N/A")
                    .sessionUrl("N/A")
                    .message("Payment failed. Please try with a different method.")
                    .build();
        }

        booking.setPaymentId(stripe.getSessionId());
        bookingService.createBooking(booking);

        Email email = new Email();
        email.setTo(booking.getEmail());
        email.setSubject("Flight Booking Confirmation");
        email.setBody(
                "Dear " + booking.getName() + ",\n\n" +
                        "Thank you for booking with us! Your flight has been successfully booked.\n\n" +
                        "Booking Details:\n" +
                        "Booking ID   : " + booking.getBookingId() + "\n" +
                        "Flight ID    : " + booking.getFlightId() + "\n" +
                        "Amount Paid  : " + booking.getPaymentAmount() + " INR\n\n" +
                        "We wish you a pleasant journey!\n\n" +
                        "Best regards,\n" +
                        "Flight Reservation Team"
        );
        notificationService.message(email);

        return stripe;
    }

    public StripeResponse paymentFallback(BookingEntity booking, Exception ex) {
        return StripeResponse.builder()
                .status("Service Unavailable")
                .sessionId("N/A")
                .sessionUrl("N/A")
                .message("Payment service is temporarily unavailable. Please try again later.")
                .build();
    }

    @GetMapping("/{id}")
    public BookingEntity getBookingById(@PathVariable String id) {
        return bookingService.getBookingById(id);
    }

    @PutMapping("/{id}")
    public BookingEntity updateBooking(@PathVariable String id, @RequestBody BookingEntity updatedBooking) {
        BookingEntity booking = bookingService.updateBooking(id, updatedBooking);

        Email email = new Email();
        email.setTo(booking.getEmail());
        email.setSubject("Flight Booking Update Confirmation");
        email.setBody(
                "Dear " + booking.getName() + ",\n\n" +
                        "Your flight booking has been successfully updated.\n\n" +
                        "Updated Booking Details:\n" +
                        "Booking ID   : " + booking.getBookingId() + "\n" +
                        "Flight ID    : " + booking.getFlightId() + "\n" +
                        "Amount Paid  : " + booking.getPaymentAmount() + " INR\n\n" +
                        "If you did not request this update or need assistance, please contact our support team.\n\n" +
                        "Thank you,\n" +
                        "Flight Reservation Team"
        );

        notificationService.message(email);
        return booking;
    }

    @DeleteMapping("/{id}")
    public String deleteBooking(@PathVariable String id) {
        BookingEntity entity = bookingService.getBookingById(id);
        if (entity == null) {
            return "Booking with ID " + id + " not found.";
        }

        Email email = new Email();
        email.setTo(entity.getEmail());
        email.setSubject("Flight Booking Cancellation Notice");
        email.setBody(
                "Dear " + entity.getName() + ",\n\n" +
                        "We would like to inform you that your flight booking has been successfully cancelled.\n\n" +
                        "Booking Details:\n" +
                        "Booking ID: " + entity.getBookingId() + "\n" +
                        "Flight ID: " + entity.getFlightId() + "\n" +
                        "Amount Paid: " + entity.getPaymentAmount() / 100 + "\n\n" +
                        "If you did not request this cancellation or have any concerns, please contact our support team immediately.\n\n" +
                        "Thank you for using our service,\n" +
                        "Flight Reservation Team"
        );

        notificationService.message(email);
        bookingService.deleteBooking(id);
        return "Booking with ID " + id + " deleted successfully.";
    }
}
