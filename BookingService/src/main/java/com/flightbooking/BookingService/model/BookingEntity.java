package com.flightbooking.BookingService.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

@Entity
@Table(name = "Bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String bookingId;
    private String userId;
    private String flightId;
    private String name;
    private int age;
    private String Email;
    private String address;
    private String fromCity;
    private String toCity;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date departure;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date arrival;

   private String paymentId;
    private Long paymentAmount;


}
