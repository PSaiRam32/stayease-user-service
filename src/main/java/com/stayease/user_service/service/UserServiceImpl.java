package com.stayease.user_service.service;

import com.stayease.user_service.dto.UpdateUserRequest;
import com.stayease.user_service.dto.UserResponse;
import com.stayease.user_service.entity.User;
import com.stayease.user_service.entity.Wishlist;
import com.stayease.user_service.exception.UserNotFoundException;
import com.stayease.user_service.repository.UserRepository;
import com.stayease.user_service.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;

    @Override
    public UserResponse getUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public UserResponse updateUser(Long id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setName(request.getName());

        userRepository.save(user);

        return getUser(id);
    }

    @Override
    public void addWishlist(Long userId, Long propertyId) {

        Wishlist wishlist = Wishlist.builder()
                .userId(userId)
                .propertyId(propertyId)
                .build();

        wishlistRepository.save(wishlist);
    }

    @Override
    public List<Long> getWishlist(Long userId) {

        return wishlistRepository.findByUserId(userId)
                .stream()
                .map(Wishlist::getPropertyId)
                .toList();
    }

    @Override
    public void createuser(User user) {
        userRepository.save(user);
    }
}