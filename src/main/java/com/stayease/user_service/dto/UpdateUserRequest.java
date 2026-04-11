package com.stayease.user_service.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Getter @Setter
public class UpdateUserRequest {

    @NotBlank
    private String name;
    private String phone;
}