package com.stayease.user_service.dto.Response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WishlistResponse {
    private Long propertyId;
    private String username;
    private LocalDateTime createdAt;
    private Object propertyDetails;
}
