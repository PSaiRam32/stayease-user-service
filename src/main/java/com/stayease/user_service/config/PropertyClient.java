package com.stayease.user_service.config;

import com.stayease.user_service.dto.PropertyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
        name = "property-service",
        url = "${services.property.url}",
        configuration = FeignClientConfig.class
)
public interface PropertyClient {

    @GetMapping("/properties/{propertyId}")
    PropertyResponse getProperties(@PathVariable Long propertyId);
}