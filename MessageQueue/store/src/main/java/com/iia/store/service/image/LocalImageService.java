package com.iia.store.service.image;

import com.iia.store.config.exception.ImageDeleteFailureException;
import com.iia.store.config.exception.ImageUploadFailureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class LocalImageService implements ImageService {

    @Value("${upload.image.location}")
    private String location;

    @PostConstruct
    void postConstruct() {
        File dir = new File(location);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    @Override
    public void upload(MultipartFile file, String filename) {
        try {
            Path targetLocation = new File(location + filename).toPath();
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch(Exception e) {
            throw new ImageUploadFailureException(e);
        }
    }
    @Override
    public void delete(String filename) {
        File file = new File(location + filename);
        boolean deleted = file.delete();
        if (!deleted) {
            throw new ImageDeleteFailureException();
        }
    }
}