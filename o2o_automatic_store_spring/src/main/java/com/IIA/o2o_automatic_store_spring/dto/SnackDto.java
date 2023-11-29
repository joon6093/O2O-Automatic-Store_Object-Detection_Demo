package com.IIA.o2o_automatic_store_spring.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
public class SnackDto {
    @NotBlank(message = "Filename cannot be blank")
    private String filename;

    @NotBlank(message = "Object name cannot be blank")
    private String object_name;

    @NotNull(message = "Position cannot be null")
    private PositionDto position;

    @Data
    public static class PositionDto {
        private int x1;
        private int x2;
        private int y1;
        private int y2;
    }
}
