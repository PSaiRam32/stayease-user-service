package com.stayease.user_service.dto.Response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long userid;
    private String name;
    private String email;
    private String role;
    private String phone;
    private LocalDateTime updatedAt;
    private String profileImageUrl;
}