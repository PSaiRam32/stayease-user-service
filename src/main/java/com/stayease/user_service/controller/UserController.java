package com.stayease.user_service.controller;


import com.stayease.user_service.dto.UpdateUserRequest;
import com.stayease.user_service.dto.UserResponse;
import com.stayease.user_service.entity.User;
import com.stayease.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth-internal")
    public void createUser(@RequestBody User user){
        userService.createuser(user);
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/{id}/wishlist")
    public void addWishlist(
            @PathVariable Long id,
            @RequestParam Long propertyId) {

        userService.addWishlist(id, propertyId);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{id}/wishlist")
    public List<Long> getWishlist(@PathVariable Long id) {
        return userService.getWishlist(id);
    }
}