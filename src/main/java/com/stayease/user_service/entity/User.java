package com.stayease.user_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {


    @Id
    private Long id;
    private String name;
    private String email;
    private String role;
    private boolean active = true;
}