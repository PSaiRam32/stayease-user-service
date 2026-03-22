package com.stayease.user_service.service;


import com.stayease.user_service.dto.UpdateUserRequest;
import com.stayease.user_service.dto.UserResponse;
import com.stayease.user_service.entity.User;

import java.util.List;

public interface UserService {

    UserResponse getUser(Long id);

    UserResponse updateUser(Long id, UpdateUserRequest request);

    void addWishlist(Long userId, Long propertyId);

    List<Long> getWishlist(Long userId);

    void createuser(User user);
}