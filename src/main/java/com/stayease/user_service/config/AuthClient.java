package com.stayease.user_service.config;

import com.stayease.user_service.dto.Request.UserDeactivationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "auth-service",
        url = "${services.auth.url}",
        configuration = FeignConfig.class
)
public interface AuthClient {

    @PutMapping("/auth/internal/users/deactivate/{userId}")
    void deactivateUser(@PathVariable Long userId,@RequestBody UserDeactivationRequest request);
}
