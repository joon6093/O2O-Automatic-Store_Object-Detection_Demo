package com.iia.store.dto.store;

import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreReadBySignInrRequest {
    @Null
    private Long memberId;
}
