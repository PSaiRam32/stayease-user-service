package com.stayease.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    private String name;
    @Column(unique = true)
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder.Default
    private boolean isActive = false;
    @Builder.Default
    private boolean emailVerified = false;
    private String profileImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}