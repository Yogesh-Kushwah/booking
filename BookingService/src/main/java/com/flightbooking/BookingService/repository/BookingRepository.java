package com.flightbooking.BookingService.repository;

import com.flightbooking.BookingService.model.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,String>{

}
