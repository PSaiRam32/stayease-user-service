package com.stayease.user_service.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger logger = LoggerFactory.getLogger(FeignErrorDecoder.class);

    @Override
    public Exception decode(String methodKey, Response response) {

        int status = response.status();
        logger.error("Feign error for method {}: status {}", methodKey, status);
        switch (status) {
            case 400:
                return new RuntimeException("Bad Request from downstream service");
            case 404:
                return new RuntimeException("Resource not found in downstream service");
            case 500:
                return new RuntimeException("Internal server error in downstream service");
            default:
                return new RuntimeException("Feign client error: " + status);
        }
    }
}