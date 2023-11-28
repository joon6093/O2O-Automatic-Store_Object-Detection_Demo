package com.IIA.o2o_automatic_store_spring.dto;

import com.IIA.o2o_automatic_store_spring.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
    private StatusEnum status;
    private String message;
    private Object data;
}