package com.beyond.university.auth.model.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import java.time.Duration;

public interface JwtCookieService {
    ResponseCookie createRefreshTokenCookie(String refreshToken, Duration duration);

    HttpHeaders createRefreshTokenCookieHeaders(ResponseCookie cookie);

    ResponseCookie deleteRefreshTokenCookie();
}
