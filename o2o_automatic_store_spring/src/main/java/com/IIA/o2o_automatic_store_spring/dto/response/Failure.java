package com.IIA.o2o_automatic_store_spring.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Failure implements Result {
    private String msg;
}