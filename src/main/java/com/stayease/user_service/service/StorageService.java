package com.stayease.user_service.service;

import org.springframework.web.multipart.MultipartFile;
/*
 ==========================================================
 STORAGE SERVICE

 Current Implementation:
 Local File System

 Future:
 Replace LocalStorageServiceImpl with

 - AwsStorageServiceImpl
 - GcpStorageServiceImpl
 - AzureBlobStorageServiceImpl

 NOTE:
 No changes required in:
 - Controller
 - UserService
 - UserServiceImpl
 - Entity
 - Database

 Only the implementation class changes.

 ==========================================================
*/

public interface StorageService{
    String uploadProfileImage(Long userId,MultipartFile file);
    void deleteProfileImage(String imageUrl);

}