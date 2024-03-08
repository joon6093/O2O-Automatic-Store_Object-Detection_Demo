package com.iia.store.dto.store;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StoreRefreshTokenResponse {
    private String userAccessToken;
}