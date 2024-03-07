package com.iia.store.service.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void upload(MultipartFile file, String filename);

    void delete(String filename);
}