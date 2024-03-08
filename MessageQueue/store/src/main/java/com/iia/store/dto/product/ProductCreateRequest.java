package com.iia.store.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {
    @NotBlank(message = "{productCreateRequest.name.notBlank}")
    private String name;

    @PositiveOrZero(message = "{productCreateRequest.price.positiveOrZero}")
    private double price;

    private List<MultipartFile> images = new ArrayList<>();

    @Null
    private Long storeId;
}