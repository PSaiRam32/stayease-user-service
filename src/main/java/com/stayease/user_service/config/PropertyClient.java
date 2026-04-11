package com.stayease.user_service.config;

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

    @GetMapping("/properties/{Id}")
    List<Object> getproperties(@PathVariable Long propertyId);
}