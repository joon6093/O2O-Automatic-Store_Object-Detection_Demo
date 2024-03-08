package com.iia.store.dto.product;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ProductListDto {
    private List<SimpleProductDto> stores;
}
