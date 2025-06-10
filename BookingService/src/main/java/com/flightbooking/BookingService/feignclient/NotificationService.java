package com.flightbooking.BookingService.feignclient;

import com.flightbooking.BookingService.dto.Email;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="NOTIFICATION-SERVICE")
public interface NotificationService {

    @PostMapping("/sendmail")
    public String message(@RequestBody Email email);
}
