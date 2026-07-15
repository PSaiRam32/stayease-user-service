package com.stayease.user_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

/*
 ==========================================================
 STORAGE CONFIGURATION

 Current:
 Serves uploaded images from local file system.

 Future Cloud Migration:
 -----------------------
 When migrating to AWS S3 / Google Cloud Storage /
 Azure Blob Storage,

 DELETE this configuration.

 Images will be served directly by the cloud provider.

 No controller changes.
 No service changes.
 No entity changes.
 No database changes.

 ==========================================================
*/

@Configuration
public class StorageConfig implements WebMvcConfigurer{
    @Value("${file.upload-dir}")
    private String uploadDirectory;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath=Paths.get(uploadDirectory);
        String uploadLocation=uploadPath.toFile().getAbsolutePath();
        registry.addResourceHandler("/uploads/**").addResourceLocations("file:" + uploadLocation + "/");
    }
}