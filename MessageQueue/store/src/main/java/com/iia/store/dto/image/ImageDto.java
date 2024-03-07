package com.iia.store.dto.image;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageDto {
    private Long id;
    private String originName;
    private String uniqueName;
}