package com.stayease.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingHistoryResponse {
    
    private Long bookingId;
    private Long userId;
    private Long propertyId;
    private String propertyName;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private Double totalPrice;
    private String bookingStatus;
    private LocalDateTime createdAt;
}
