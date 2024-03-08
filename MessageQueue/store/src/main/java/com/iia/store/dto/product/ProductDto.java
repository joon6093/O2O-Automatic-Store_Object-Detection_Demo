package com.iia.store.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iia.store.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private List<ImageDto> images;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime modifiedAt;
}
