package com.iia.store.entity.store;

import com.iia.store.config.exception.UnsupportedImageFormatException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String uniqueName;


    private final static String[] supportedExtension = {"jpg", "jpeg", "gif", "bmp", "png"};

    @Builder
    public StoreImage(String originName) {
        this.originName = originName;
        this.uniqueName = generateUniqueName(extractExtension(originName));
    }

    private String generateUniqueName(String extension) {
        return UUID.randomUUID() + "." + extension;
    }

    private String extractExtension(String originName) {
        int lastIndexOfDot = originName.lastIndexOf(".");
        if (lastIndexOfDot == -1 || lastIndexOfDot == originName.length() - 1) {
            throw new UnsupportedImageFormatException();
        }
        String ext = originName.substring(lastIndexOfDot + 1).toLowerCase();
        if (!isSupportedFormat(ext)) {
            throw new UnsupportedImageFormatException();
        }
        return ext;
    }


    private boolean isSupportedFormat(String ext) {
        return Arrays.stream(supportedExtension).anyMatch(e -> e.equalsIgnoreCase(ext));
    }
}