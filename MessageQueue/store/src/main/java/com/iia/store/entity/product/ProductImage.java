package com.iia.store.entity.product;

import com.iia.store.config.exception.UnsupportedImageFormatException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Arrays;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String originName;

    @Column(nullable = false)
    private String uniqueName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Product product;

    private final static String[] supportedExtension = {"jpg", "jpeg", "gif", "bmp", "png"};

    @Builder
    public ProductImage(String originName) {
        this.originName = originName;
        this.uniqueName = generateUniqueName(extractExtension(originName));
    }

    public void setProduct(Product product) {
        if(this.product == null) {
            this.product = product;
        }
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