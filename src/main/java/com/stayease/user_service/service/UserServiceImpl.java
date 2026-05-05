package com.stayease.user_service.service;

import com.stayease.user_service.config.BookingClient;
import com.stayease.user_service.config.PropertyClient;
import com.stayease.user_service.dto.*;
import com.stayease.user_service.entity.User;
import com.stayease.user_service.entity.Wishlist;
import com.stayease.user_service.exception.PropertyNotFoundException;
import com.stayease.user_service.exception.UserNotFoundException;
import com.stayease.user_service.repository.UserRepository;
import com.stayease.user_service.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final BookingClient bookingClient;
    private final PropertyClient propertyClient;


    public UserResponse getUser(Long id) {
        logger.info("Retrieving user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        logger.info("Updating user: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return mapToResponse(user);
    }

    public void addToWishlist(Long userId, WishlistRequest request) {
        logger.info("Adding property {} to wishlist for user {}", request.getPropertyId(), userId);
        // Check if property exists
        PropertyResponse response = propertyClient.getProperties(request.getPropertyId());
        if (response == null || response.getData() == null) {
            logger.error("Property {} does not exist", request.getPropertyId());
            throw new PropertyNotFoundException("Property not found");
        }
        // Check if already exists
        List<Wishlist> existing = wishlistRepository.findByUserId(userId);
        boolean alreadyExists = existing.stream().anyMatch(w -> w.getPropertyId().equals(request.getPropertyId()));
        if (alreadyExists) {
            logger.warn("Property {} already in wishlist for user {}", request.getPropertyId(),userId);
            return;
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setPropertyId(request.getPropertyId());
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlistRepository.save(wishlist);
        logger.info("Successfully added property {} to wishlist for user {}", request.getPropertyId(),userId);
    }

    public List<WishlistResponse> getWishlist(Long Id) {
        logger.info("Retrieving wishlist for user: {}",Id);
        return wishlistRepository.findByUserId(Id)
                .stream()
                .map(this::mapToWishlistResponse)
                .toList();
    }

    public void removeFromWishlist(Long  Id, WishlistRequest request) {
        logger.info("Removing property {} from wishlist for user {}", request.getPropertyId(), Id);
        List<Wishlist> wishlists = wishlistRepository.findByUserId(Id);
        Wishlist toRemove = wishlists.stream()
                .filter(w -> w.getPropertyId().equals(request.getPropertyId()))
                .findFirst()
                .orElse(null);
        if (toRemove != null) {
            wishlistRepository.delete(toRemove);
            logger.info("Successfully removed property {} from wishlist for user {}", request.getPropertyId(), Id);
        } else {
            logger.warn("Property {} not found in wishlist for user {}", request.getPropertyId(), Id);
        }
    }

    @Override
    public void createUser(User user) {
//        if (userRepository.existsById(user.getUserid())) {
//            return;
//        }
        logger.info("Creating new user: {}", user.getEmail());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        logger.info("User created successfully: {}", user.getEmail());
    }

    public void deleteUser(Long userId) {
        logger.info("Deleting user: {}", userId);
        userRepository.deleteById(userId);
        logger.info("User deleted successfully: {}", userId);
    }



    public List<BookingHistoryResponse> getBookingHistory(Long userId) {
        logger.info("Fetching booking history for user: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        logger.debug("User verified for booking history: {}", userId);
        
//        List<Object> bookings = bookingClient.getUserBookings(userId);
        ApiResponse<List<BookingHistoryResponse>> response = bookingClient.getUserBookings(userId);
        List<BookingHistoryResponse> bookings = response.getData();
        logger.info("Retrieved {} bookings for user: {}", bookings.size(), userId);
        return bookings;
//        return bookings.stream()
//                .map(booking -> {
//                    // Convert booking object to BookingHistoryResponse
//                    // This assumes bookingClient returns booking data as Map or similar
//                    logger.debug("Processing booking for user: {}", userId);
//                    return new BookingHistoryResponse();
//                })
//                .toList();
    }

    private UserResponse mapToResponse(User user) {
        return UserResponse.builder()
                .userid(user.getUserid())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(String.valueOf(user.getRole()))
                .build();
    }

    private WishlistResponse mapToWishlistResponse(Wishlist wishlist) {
        WishlistResponse response = new WishlistResponse();
        response.setPropertyId(wishlist.getPropertyId());
        response.setCreatedAt(wishlist.getCreatedAt());
        // Fetch property details
        PropertyResponse  properties = propertyClient.getProperties(wishlist.getPropertyId());
        if (properties != null && properties.getData() != null) {
            response.setPropertyDetails(properties.getData());
        }
        return response;
    }
}