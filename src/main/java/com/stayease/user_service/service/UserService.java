package com.stayease.user_service.service;


import com.stayease.user_service.dto.*;

import java.util.List;

public interface UserService {

    UserResponse getUser(Long userid);
    UserResponse updateUser(Long userid, UpdateUserRequest request);
    void addToWishlist(Long userId, WishlistRequest request);
    List<WishlistResponse> getWishlist(Long userId);
    void removeFromWishlist(Long userId, WishlistRequest request);
    void createUser(UserProfileRequest request);
    List<BookingHistoryResponse> getBookingHistory(Long userId);
    void deleteUser(Long userId);
    void verifyUser(Long userId, UserVerificationRequest request);
}