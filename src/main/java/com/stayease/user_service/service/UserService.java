package com.stayease.user_service.service;


import com.stayease.user_service.dto.*;
import com.stayease.user_service.entity.User;

import java.util.List;

public interface UserService {

    UserResponse getUser(Long id);
    UserResponse updateUser(Long id, UpdateUserRequest request);
    void addToWishlist(Long Id, WishlistRequest request);
    List<WishlistResponse> getWishlist(Long userId);
    void removeFromWishlist(Long userId, WishlistRequest request);
    void createuser(User user);
    List<BookingHistoryResponse> getBookingHistory(Long Id);
}