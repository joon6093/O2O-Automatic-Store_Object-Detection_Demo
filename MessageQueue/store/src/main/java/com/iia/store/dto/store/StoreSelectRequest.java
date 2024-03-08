package com.iia.store.dto.store;

import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreSelectRequest {
    @PositiveOrZero(message = "{storeSelectRequest.storeId.positiveOrZero}")
    private Long storeId;

    @Null
    private Long memberId;
}
