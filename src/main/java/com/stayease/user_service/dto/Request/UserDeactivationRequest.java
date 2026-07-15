package com.stayease.user_service.dto.Request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDeactivationRequest {
    private boolean active;
}