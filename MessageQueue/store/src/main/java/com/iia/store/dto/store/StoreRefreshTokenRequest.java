package com.iia.store.dto.store;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreRefreshTokenRequest {
    @NotBlank(message = "{storesRefreshTokenRequest.expiredStoreToken.notBlank}")
    private String expiredStoreToken;
}
