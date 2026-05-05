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
    private String userId;
    private Long propertyId;
    private Long roomId;
    private String bookingStatus;
    private Double totalPrice;
}
