package com.iia.store.dto.product;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateRequest {
    @PositiveOrZero(message = "{stockUpdateRequest.productId.positiveOrZero}")
    private Long productId;

    @PositiveOrZero(message = "{stockUpdateRequest.newQuantity.positiveOrZero}")
    private int quantity;
}
