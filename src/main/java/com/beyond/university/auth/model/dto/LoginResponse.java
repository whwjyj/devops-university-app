package com.beyond.university.auth.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@Builder
@RequiredArgsConstructor
public class LoginResponse {
    private final String accessToken;

    private final String type;

    private final String username;

    private final List<String> roles;

    private final long issuedAt;

    private final long expiresAt;
}