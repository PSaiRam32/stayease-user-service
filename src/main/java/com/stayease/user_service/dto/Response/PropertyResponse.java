package com.stayease.user_service.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyResponse {
    private String status;
    private String message;
    private Object data;
}