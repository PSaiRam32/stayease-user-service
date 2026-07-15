package com.stayease.user_service.service;


import com.stayease.user_service.dto.Request.UpdateUserRequest;
import com.stayease.user_service.dto.Request.UserProfileRequest;
import com.stayease.user_service.dto.Request.UserVerificationRequest;
import com.stayease.user_service.dto.Request.WishlistRequest;
import com.stayease.user_service.dto.Response.BookingHistoryResponse;
import com.stayease.user_service.dto.Response.ProfileImageResponse;
import com.stayease.user_service.dto.Response.UserResponse;
import com.stayease.user_service.dto.Response.WishlistResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    UserResponse getUser(Long userid);
    UserResponse updateUser(Long userid, UpdateUserRequest request);
    void addToWishlist(Long userId, WishlistRequest request);
    List<WishlistResponse> getWishlist(Long userId);
    void removeFromWishlist(Long userId, WishlistRequest request);
    void createUser(UserProfileRequest request);
    List<BookingHistoryResponse> getBookingHistory(Long userId);
//    void deleteUser(Long userId);
    void verifyUser(Long userId, UserVerificationRequest request);
    ProfileImageResponse uploadProfileImage(Long userId, MultipartFile file);
    void deactivateUser(Long userId);
}