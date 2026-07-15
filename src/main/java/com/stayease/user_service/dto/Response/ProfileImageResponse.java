package com.stayease.user_service.dto.Response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileImageResponse{
    private String message;
    private String imageUrl;
}