package com.iia.store.dto.store;

import com.iia.store.config.aop.ValidImageFile;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreCreateRequest {
    @NotBlank(message = "{storeCreateRequest.name.notBlank}")
    private String name;

    @NotBlank(message = "{storeCreateRequest.description.notBlank}")
    private String description;

    @ValidImageFile(message = "{storeCreateRequest.image.ValidImage}")
    private MultipartFile image;

    @Null
    private Long memberId;
}
