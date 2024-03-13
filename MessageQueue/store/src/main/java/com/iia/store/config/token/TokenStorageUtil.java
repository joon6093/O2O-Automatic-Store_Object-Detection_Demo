package com.iia.store.config.token;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class TokenStorageUtil {
    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String REFRESH_TOKEN_NAME = "refreshToken";

    public static void addAccessTokenInHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(ACCESS_TOKEN_NAME, accessToken);
    }

    public static void addRefreshTokenInCookie(HttpServletResponse response, String refreshToken, int maxAgeInSeconds) {
        String encodedRefreshToken = URLEncoder.encode(refreshToken, StandardCharsets.UTF_8);
        Cookie refreshCookie = new Cookie(REFRESH_TOKEN_NAME, encodedRefreshToken);
        refreshCookie.setMaxAge(maxAgeInSeconds);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        // refreshCookie.setSecure(true);
        response.addCookie(refreshCookie);
    }
}
