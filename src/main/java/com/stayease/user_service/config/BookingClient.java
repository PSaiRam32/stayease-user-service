package com.stayease.user_service.config;

import com.stayease.user_service.dto.Response.ApiResponse;
import com.stayease.user_service.dto.Response.BookingHistoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "booking-service",
        url = "${services.booking.url}",
        configuration = FeignConfig.class
)
public interface BookingClient {

    @GetMapping("/bookings/user/{userId}")
    ApiResponse<List<BookingHistoryResponse>> getUserBookings(@PathVariable Long userId);
}