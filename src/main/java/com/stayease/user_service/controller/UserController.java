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
import org.springframework.http.ResponseEntity;
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
        userService.createUser(user);
    }

    @DeleteMapping("/delete-internal/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary="Get User Details")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    @GetMapping("/{userid}")
    public UserResponse getUser(@PathVariable Long userid) {
        logger.info("Fetching user with id: {}", userid);
        return userService.getUser(userid);
    }

    @Operation(summary="Update User Details")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    @PutMapping("/update/{userid}")
    public UserResponse updateUser(
            @PathVariable Long userid,
            @Valid @RequestBody UpdateUserRequest request) {
        logger.info("Updating user with id: {}", userid);
        return userService.updateUser(userid, request);
    }

    @Operation(summary="Add Wishlist")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addwishlist/{userid}")
    public void addToWishlist(
            @PathVariable Long userid,
            @RequestBody WishlistRequest request) {
        logger.info("Adding property {} to wishlist for user {}", request.getPropertyId(), userid);
        userService.addToWishlist(userid, request);
    }

    @Operation(summary="Get Wishlist")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getwishlist/{userid}")
    public List<WishlistResponse> getWishlist(@PathVariable Long userid) {
        logger.info("Getting wishlist for user: {}", userid);
        return userService.getWishlist(userid);
    }

    @Operation(summary="Remove Wishlist")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/removewishlist/{userid}")
    public void removeFromWishlist(
            @PathVariable Long userid,
            @RequestBody WishlistRequest request) {
        logger.info("Removing property {} from wishlist for user {}", request.getPropertyId(), userid);
        userService.removeFromWishlist(userid, request);
    }

    @Operation(summary="Get Bookings")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getbookings/{userid}")
    public List<BookingHistoryResponse> getBookings(@PathVariable Long userid) {
        logger.info("Getting bookings for user: {}", userid);
        return userService.getBookingHistory(userid);
    }
}