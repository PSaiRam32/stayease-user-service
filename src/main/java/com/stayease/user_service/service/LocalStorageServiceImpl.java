package com.stayease.user_service.service;

import com.stayease.user_service.exception.FileStorageException;
import com.stayease.user_service.exception.InvalidFileException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class LocalStorageServiceImpl implements StorageService {

    @Value("${file.upload-dir}")
    private String uploadDirectory;
    @Value("${file.max-file-size}")
    private DataSize maxFileSize;
//    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;
    private static final List<String> ALLOWED_CONTENT_TYPES = List.of(
            "image/jpeg","image/png","image/jpg","image/webp");
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg",".jpeg",".png",".webp");
    private static final Tika tika = new Tika();

    @Override
    public String uploadProfileImage(Long userId,MultipartFile file){
        log.info("Uploading profile image for userId : {}", userId);
        validateFile(file);
        try{
            Path uploadPath=createUploadDirectory();
            /*
            CURRENT IMPLEMENTATION - Stores images on Local File System.
            FUTURE IMPLEMENTATION -
            Replace this class with AwsStorageServiceImpl or GcpStorageServiceImpl or AzureBlobStorageServiceImpl
            No changes required in Controller,UserService,User Entity,Repository,Database
            Only StorageService implementation changes. */
            String fileName=generateFileName(userId,file);
            Path targetLocation=uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(),targetLocation,StandardCopyOption.REPLACE_EXISTING);
            log.info("Profile image uploaded successfully for userId: {}", userId);
            return "/uploads/profile-images/" + fileName;
        }
        catch (IOException ex){
            log.error("Unable to upload profile image", ex);
            throw new FileStorageException("Unable to upload profile image");
        }
    }

    private Path createUploadDirectory() throws IOException{
        Path uploadPath=Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
            log.info("Created upload directory : {}",uploadPath);
        }
        return uploadPath;
    }

    private String generateFileName(Long userId,MultipartFile file){
        String originalFilename=StringUtils.cleanPath(file.getOriginalFilename());
        int index=originalFilename.lastIndexOf(".");
        String extension=originalFilename.substring(index);
        return userId
                + "_"
                + UUID.randomUUID()
                + extension;
    }

    private void validateFile(MultipartFile file){
        if (file == null || file.isEmpty()){
            throw new InvalidFileException("Please upload a valid image.");
        }
        log.info("Original filename : {}", file.getOriginalFilename());
        log.info("Content type : {}", file.getContentType());
        log.info("File size : {}", file.getSize());
        if (file.getSize() > maxFileSize.toBytes()){
            throw new InvalidFileException("Maximum allowed file size is 5 MB.");
        }
        validateUploadedFile(file);
    }

    private void validateUploadedFile(MultipartFile file){
        String fileName = file.getOriginalFilename();
        if (fileName == null){
            throw new InvalidFileException("Invalid file.");
        }
        int index = fileName.lastIndexOf(".");
        if (index < 0){
            throw new InvalidFileException("File extension is missing.");
        }
        String extension=fileName.substring(index).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)){
            throw new InvalidFileException("Only JPG, JPEG, PNG and WEBP images are allowed.");
        }

        String contentType = file.getContentType();
        if (!(ALLOWED_CONTENT_TYPES.contains(contentType) || "application/octet-stream".equals(contentType))){
            throw new InvalidFileException("Invalid image type.");
        }
        // Magic Byte - Every file type has a secret signature hidden at the very beginning of its raw data
        //    PNG - 89 50 4E 47
        //    JPEG - FF D8 FF
        try {
            String detectedType = tika.detect(file.getInputStream());
            log.debug("Detected actual MIME Type via Tika: {}", detectedType);
            if (!ALLOWED_CONTENT_TYPES.contains(detectedType)) {
                throw new InvalidFileException("Uploaded file content is not a valid image format.");
            }
        } catch (IOException ex) {
            throw new FileStorageException("Unable to read and validate the uploaded file content.");
        }
    }


    @Override
    public void deleteProfileImage(String imageUrl){
        if (imageUrl == null || imageUrl.isBlank()){
            return;
        }
        if (imageUrl.endsWith("default-avatar.png")){
            return;
        }
        try {
        /*
         FUTURE CLOUD STORAGE MIGRATION
         Replace local file deletion with
         amazonS3.deleteObject(...) or googleCloudStorage.delete(...) or azureBlobClient.delete();
         */
            String fileName=Paths.get(imageUrl).getFileName().toString();
            Path filePath=Paths.get(uploadDirectory).resolve(fileName);
            boolean deleted=Files.deleteIfExists(filePath);
            if(deleted){
                log.info("Deleted old profile image : {}",fileName);
            } else{
                log.debug("Profile image not found : {}",fileName);
            }
        }
        catch (IOException ex){
            log.error("Unable to delete profile image", ex);
            throw new FileStorageException("Unable to delete old profile image.");
        }
    }
}