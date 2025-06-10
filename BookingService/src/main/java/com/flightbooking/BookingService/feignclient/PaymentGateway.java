package com.flightbooking.BookingService.feignclient;

import com.flightbooking.BookingService.dto.BookingRequest;
import com.flightbooking.BookingService.dto.StripeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@FeignClient(name="PAYMENT-GATEWAY")
public interface PaymentGateway {

    @PostMapping("/payment/checkout")
    public StripeResponse checkoutProduct(@RequestBody BookingRequest bookingRequest);

//    StripeResponse checkoutProduct(String name, Long paymentAmount, int totalTickets, String currency);
}
