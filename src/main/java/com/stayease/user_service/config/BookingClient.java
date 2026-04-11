package com.stayease.user_service.config;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "booking-service",
        url = "${services.booking.url}",
        configuration = FeignClientConfig.class
)
public interface BookingClient {

    @GetMapping("/bookings/user/{userId}")
    List<Object> getUserBookings(@PathVariable Long userId);
}