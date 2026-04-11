package com.stayease.user_service.controller;


import com.stayease.user_service.dto.UpdateUserRequest;
import com.stayease.user_service.dto.UserResponse;
import com.stayease.user_service.dto.WishlistRequest;
import com.stayease.user_service.dto.WishlistResponse;
import com.stayease.user_service.dto.BookingHistoryResponse;
import com.stayease.user_service.entity.User;
import com.stayease.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "User Controller", description = "User Service APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @PostMapping("/auth-internal")
    public void createUser(@RequestBody User user){
        logger.info("Creating new user: {}", user.getEmail());
        userService.createuser(user);
    }

    @Operation(summary="Get User Details")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        logger.info("Fetching user with id: {}", id);
        return userService.getUser(id);
    }

    @Operation(summary="Update User Details")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    @PutMapping("/update/{id}")
    public UserResponse updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        logger.info("Updating user with id: {}", id);
        return userService.updateUser(id, request);
    }

    @Operation(summary="Add Wishlist")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addwishlist/{id}")
    public void addToWishlist(
            @PathVariable Long id,
            @RequestBody WishlistRequest request) {
        logger.info("Adding property {} to wishlist for user {}", request.getPropertyId(), id);
        userService.addToWishlist(id, request);
    }

    @Operation(summary="Get Wishlist")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getwishlist/{id}")
    public List<WishlistResponse> getWishlist(@PathVariable Long id) {
        logger.info("Getting wishlist for user: {}", id);
        return userService.getWishlist(id);
    }

    @Operation(summary="Remove Wishlist")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/removewishlist/{id}")
    public void removeFromWishlist(
            @PathVariable Long id,
            @RequestBody WishlistRequest request) {
        logger.info("Removing property {} from wishlist for user {}", request.getPropertyId(), id);
        userService.removeFromWishlist(id, request);
    }

    @Operation(summary="Get Bookings")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/gtbookings/{id}")
    public List<BookingHistoryResponse> getBookings(@PathVariable Long Id) {
        logger.info("Getting bookings for user: {}", Id);
        return userService.getBookingHistory(Id);
    }
}