package com.iia.store.dto.product;

import com.iia.store.config.aop.ValidImageFileList;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "{productCreateRequest.name.notBlank}")
    private String name;

    @PositiveOrZero(message = "{productCreateRequest.price.positiveOrZero}")
    private double price;

    @ValidImageFileList(message = "{productCreateRequest.images.ValidImage}")
    private List<MultipartFile> images;

    @Null
    private Long storeId;
}