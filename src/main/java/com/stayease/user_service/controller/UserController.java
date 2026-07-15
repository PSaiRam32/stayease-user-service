package com.stayease.user_service.controller;


import com.stayease.user_service.dto.Request.UpdateUserRequest;
import com.stayease.user_service.dto.Request.UserProfileRequest;
import com.stayease.user_service.dto.Request.UserVerificationRequest;
import com.stayease.user_service.dto.Request.WishlistRequest;
import com.stayease.user_service.dto.Response.BookingHistoryResponse;
import com.stayease.user_service.dto.Response.ProfileImageResponse;
import com.stayease.user_service.dto.Response.UserResponse;
import com.stayease.user_service.dto.Response.WishlistResponse;
import com.stayease.user_service.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@Tag(name = "User Controller", description = "User Service APIs")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/auth-internal")
    public void createUser(@RequestBody UserProfileRequest request){
        log.info("Creating new user: {}", request.getEmail());
        userService.createUser(request);
    }

    @PutMapping("/auth-internal/verify/{userId}")
    public void verifyUser(@PathVariable Long userId,@RequestBody UserVerificationRequest request) {
        log.info("Received verification update for user {}", userId);
        userService.verifyUser(userId, request);
    }

    @DeleteMapping("/delete-internal/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        log.info("DELETE /users/{} - Deactivation request received.", userId);
        userService.deactivateUser(userId);
        log.info("DELETE /users/{} - User account deactivated successfully.", userId);
        return ResponseEntity.ok("User account deactivated successfully.");
    }

    @Operation(summary="Get User Details")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN') or hasRole('OWNER')")
    @GetMapping("/{userid}")
    public UserResponse getUser(@PathVariable Long userid) {
        log.info("Fetching user with id: {}", userid);
        return userService.getUser(userid);
    }

    @Operation(summary="Update User Details")
    @PreAuthorize("hasRole('USER') or hasRole('OWNER') or hasRole('ADMIN')")
    @PutMapping("/update/{userid}")
    public UserResponse updateUser(
            @PathVariable Long userid,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Updating user with id: {}", userid);
        return userService.updateUser(userid, request);
    }

    @Operation(summary="Add Wishlist")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/addwishlist/{userid}")
    public void addToWishlist(
            @PathVariable Long userid,
            @RequestBody WishlistRequest request) {
        log.info("Adding property {} to wishlist for user {}", request.getPropertyId(), userid);
        userService.addToWishlist(userid, request);
    }

    @Operation(summary="Get Wishlist")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getwishlist/{userid}")
    public List<WishlistResponse> getWishlist(@PathVariable Long userid) {
        log.info("Getting wishlist for user: {}", userid);
        return userService.getWishlist(userid);
    }

    @Operation(summary="Remove Wishlist")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/removewishlist/{userid}")
    public void removeFromWishlist(
            @PathVariable Long userid,
            @RequestBody WishlistRequest request) {
        log.info("Removing property {} from wishlist for user {}", request.getPropertyId(), userid);
        userService.removeFromWishlist(userid, request);
    }

    @Operation(summary="Get Bookings")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/getbookings/{userid}")
    public List<BookingHistoryResponse> getBookings(@PathVariable Long userid) {
        log.info("Getting bookings for user: {}", userid);
        return userService.getBookingHistory(userid);
    }

    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    @Operation(summary = "Upload Profile Picture")
    @PostMapping(value = "/profile-picture/{userId}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileImageResponse> uploadProfilePicture(@PathVariable Long userId,
                                                                     @RequestParam("file") MultipartFile file){
        log.info("POST /profile-picture/users/{} - Upload request received",userId);
        ProfileImageResponse response=userService.uploadProfileImage(userId,file);
        log.info("POST /profile-picture/users/{} - Upload successful", userId);
        return ResponseEntity.ok(response);
    }
}