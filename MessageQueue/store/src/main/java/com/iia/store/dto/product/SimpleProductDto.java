package com.iia.store.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iia.store.dto.image.ImageDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SimpleProductDto {
    private Long id;
    private String name;
    private double price;
    private ImageDto image;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
}
