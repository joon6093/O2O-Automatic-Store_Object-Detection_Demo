package com.iia.store.dto.sign;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserRefreshTokenResponse {
    private String userAccessToken;
}