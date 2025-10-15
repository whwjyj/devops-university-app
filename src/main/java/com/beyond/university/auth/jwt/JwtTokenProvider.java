package com.beyond.university.auth.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*
    JwtTokenProvider
      - 엑세스 토큰(Access Token), 리프레시 토큰(Refresh), 인증 객체(Authentication)를 생성한다.
      - 레디스(Redis)에 토큰 저장, 조회, 삭제 등의 작업을 수행한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;
    private static final long ACCESS_TOKEN_EXPIRATION = 1000L * 60L * 15; // 15분
    private static final long REFRESH_TOKEN_EXPIRATION = 1000L * 60L * 60L * 24L; // 1일

    // 엑세스 토큰(Access Token)을 생성하는 메소드
    public String createAccessToken(String username, List<String> roles) {
        Map<String, Object> claims = Map.of("username", username, "roles", roles, "token_type", "access");

        return jwtUtil.createJwtToken(claims, ACCESS_TOKEN_EXPIRATION);
    }

    // 리프레시 토큰(Refresh Token)을 생성하는 메소드
    public String createRefreshToken(String username) {
        Map<String, Object> claims = Map.of("username", username, "token_type", "refresh");
        String refreshToken = jwtUtil.createJwtToken(claims, REFRESH_TOKEN_EXPIRATION);

        // 레디스(Redis)에 리프레시 토큰(Refresh Token)을 저장
        // 리프레시 토큰(Refresh Token)의 만료 시간 동안만 Redis에 토큰을 저장
        redisTemplate.opsForValue().set(
                "refresh:" + username, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);

        return refreshToken;
    }


    // 클라이언트가 헤더를 통해 서버로 전달한 토큰을 추출하는 메소드
    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    // 액세스 토큰(Access Token)의 무결성과 유효성을 검증하는 메소드 & 블랙리스트 확인 & 엑세스 토큰 확인
    public boolean isUsableAccessToken(String accessToken) {

        return accessToken != null
                && jwtUtil.validateToken(accessToken)
                && !isBlacklisted(accessToken)
                && isAccessToken(accessToken);
    }

    // SecurityContext 객체에 저장될 Authentication 객체를 생성하는 메소드
    public Authentication createAuthentication(String accessToken) {
        String username = jwtUtil.getUsername(accessToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 로그아웃 시 블랙리스트에 액세스 토큰(Access Token)을 저장하는 메소드
    public void addBlacklist(String accessToken) {
        String key = "blacklist:" + jwtUtil.getJti(accessToken);

        // 액세스 토큰(Access Token)의 만료 시간 동안만 Redis에 토큰을 저장
        redisTemplate.opsForValue().set(key, accessToken, ACCESS_TOKEN_EXPIRATION, TimeUnit.MILLISECONDS);
    }

    // 리프레시 토큰(Refresh Token)을 삭제하는 메소드
    public void deleteRefreshToken(String accessToken) {
        String username = jwtUtil.getUsername(accessToken);

        redisTemplate.delete("refresh:" + username);
    }

    // 리프레시 토큰의 유효성을 검증하는 메소드
    public boolean isValidRefreshToken(String refreshToken) {
        String username = jwtUtil.getUsername(refreshToken);
        String storedRefreshToken = redisTemplate.opsForValue().get("refresh:" + username);

        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);
    }

    // 블랙리스트 등록 여부를 확인하는 메소드
    private boolean isBlacklisted(String accessToken) {
        String key = "blacklist:" + jwtUtil.getJti(accessToken);

        return redisTemplate.hasKey(key);
    }

    // 액세스 토큰(Access Token) 여부를 확인하는 메소드
    private boolean isAccessToken(String accessToken) {

        return jwtUtil.getTokenType(accessToken).equals("access");
    }


}
