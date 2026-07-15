package com.stayease.user_service.service;

import com.stayease.user_service.config.AuthClient;
import com.stayease.user_service.config.BookingClient;
import com.stayease.user_service.config.PropertyClient;
import com.stayease.user_service.dto.Request.*;
import com.stayease.user_service.dto.Response.*;
import com.stayease.user_service.entity.User;
import com.stayease.user_service.entity.Wishlist;
import com.stayease.user_service.exception.PropertyNotFoundException;
import com.stayease.user_service.exception.UserNotFoundException;
import com.stayease.user_service.repository.UserRepository;
import com.stayease.user_service.repository.WishlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final WishlistRepository wishlistRepository;
    private final BookingClient bookingClient;
    private final PropertyClient propertyClient;
    private final StorageService storageService;
    @Value("${file.default-avatar}")
    private String defaultAvatar;
    private final AuthClient authClient;


    public UserResponse getUser(Long id) {
        log.info("Retrieving user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        return mapToResponse(user);
    }

    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.info("Updating user: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setName(request.getName());
        user.setPhone(request.getPhone());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        return mapToResponse(user);
    }

    public void addToWishlist(Long userId, WishlistRequest request) {
        log.info("Adding property {} to wishlist for user {}", request.getPropertyId(), userId);
        // Check if property exists
        PropertyResponse response = propertyClient.getProperties(request.getPropertyId());
        if (response == null || response.getData() == null) {
            log.error("Property {} does not exist", request.getPropertyId());
            throw new PropertyNotFoundException("Property not found");
        }
        // Check if already exists
        List<Wishlist> existing = wishlistRepository.findByUserId(userId);
        boolean alreadyExists = existing.stream().anyMatch(w -> w.getPropertyId().equals(request.getPropertyId()));
        if (alreadyExists) {
            log.warn("Property {} already in wishlist for user {}", request.getPropertyId(),userId);
            return;
        }
        Wishlist wishlist = new Wishlist();
        wishlist.setUserId(userId);
        wishlist.setPropertyId(request.getPropertyId());
        wishlist.setCreatedAt(LocalDateTime.now());
        wishlistRepository.save(wishlist);
        log.info("Successfully added property {} to wishlist for user {}", request.getPropertyId(),userId);
    }

    public List<WishlistResponse> getWishlist(Long Id) {
        log.info("Retrieving wishlist for user: {}",Id);
        return wishlistRepository.findByUserId(Id)
                .stream()
                .map(this::mapToWishlistResponse)
                .toList();
    }

    public void removeFromWishlist(Long  Id, WishlistRequest request) {
        log.info("Removing property {} from wishlist for user {}", request.getPropertyId(), Id);
        List<Wishlist> wishlists = wishlistRepository.findByUserId(Id);
        Wishlist toRemove = wishlists.stream()
                .filter(w -> w.getPropertyId().equals(request.getPropertyId()))
                .findFirst()
                .orElse(null);
        if (toRemove != null) {
            wishlistRepository.delete(toRemove);
            log.info("Successfully removed property {} from wishlist for user {}", request.getPropertyId(), Id);
        } else {
            log.warn("Property {} not found in wishlist for user {}", request.getPropertyId(), Id);
        }
    }

    @Override
    public void createUser(UserProfileRequest request) {
//        if (userRepository.existsById(user.getUserid())) {
//            return;
//        }
        log.info("Creating new user: {}", request.getEmail());
        User user = User.builder()
                .userid(request.getUserId())
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .role(request.getRole())
                .isActive(request.isActive())
                .emailVerified(request.isEmailVerified())
                .createdAt(request.getCreatedAt())
                .updatedAt(request.getUpdatedAt())
                .build();
        userRepository.save(user);
        log.info("User created successfully: {}", user.getEmail());
    }

    @Override
    public void verifyUser(Long userId, UserVerificationRequest request){
        log.info("Updating verification status for user {}", userId);
        User user = userRepository.findById(userId).orElseThrow(() ->
                        new UserNotFoundException("User not found"));
        user.setActive(request.isActive());
        user.setEmailVerified(request.isEmailVerified());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("User verification status updated successfully");
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId){
        log.info("Deactivation request received for userId: {}", userId);
        User user=userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found."));
        if (!user.isActive()){
            log.warn("User {} is already deactivated.", userId);
            return;
        }
        user.setActive(false);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        authClient.deactivateUser(userId, UserDeactivationRequest.builder()
                        .active(false)
                        .build());
        log.info("User account deactivated successfully. UserId: {}", userId);
    }



    public List<BookingHistoryResponse> getBookingHistory(Long userId) {
        log.info("Fetching booking history for user: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        log.debug("User verified for booking history: {}", userId);
        
//        List<Object> bookings = bookingClient.getUserBookings(userId);
        ApiResponse<List<BookingHistoryResponse>> response = bookingClient.getUserBookings(userId);
        List<BookingHistoryResponse> bookings = response.getData();
        log.info("Retrieved {} bookings for user: {}", bookings.size(), userId);
        return bookings;
//        return bookings.stream()
//                .map(booking -> {
//                    // Convert booking object to BookingHistoryResponse
//                    // This assumes bookingClient returns booking data as Map or similar
//                    log.debug("Processing booking for user: {}", userId);
//                    return new BookingHistoryResponse();
//                })
//                .toList();
    }

    @Override
    @Transactional
    public ProfileImageResponse uploadProfileImage(Long userId, MultipartFile file){
        log.info("Uploading profile image for userId : {}", userId);
        User user=userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found"));
        String oldImage=user.getProfileImageUrl();
        // Upload first
        String newImage=storageService.uploadProfileImage(userId, file);
        // Update DB
        user.setProfileImageUrl(newImage);
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
        // Delete old image only after successful save
        storageService.deleteProfileImage(oldImage);
        return ProfileImageResponse.builder()
                .message("Profile image uploaded successfully.")
                .imageUrl(newImage)
                .build();
    }

    private UserResponse mapToResponse(User user) {
        String profileImageUrl = user.getProfileImageUrl();
        if(profileImageUrl==null||profileImageUrl.isBlank()){
            profileImageUrl =defaultAvatar;
        }
        return UserResponse.builder()
                .userid(user.getUserid())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(String.valueOf(user.getRole()))
                .profileImageUrl(profileImageUrl)
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