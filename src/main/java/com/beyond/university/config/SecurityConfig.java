package com.beyond.university.config;

import com.beyond.university.auth.handler.AccessDeniedHandlerImpl;
import com.beyond.university.auth.handler.AuthenticationEntryPointImpl;
import com.beyond.university.auth.jwt.JwtAuthenticationFilter;
import com.beyond.university.auth.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider jwtTokenProvider) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                // JWT 토큰으로 인증을 처리하기 때문에 세션을 사용하지 않는다.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 앞에 추가하는 설정
                // JwtAuthenticationFilter에서 인증이 정상적으로 처리되면 UsernamePasswordAuthenticationFilter는 자동으로 통과한다.
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtTokenProvider),
                        UsernamePasswordAuthenticationFilter.class
                )
                //                람다식으로 상태 코드만 전달하도록 구현
                //                .exceptionHandling(exceptionHandling ->
                //                    exceptionHandling
                //                        // 인증되지 않은 사용자가 리소스에 접근할 때 호출되는 핸들러이다.
                //                        // 401 Unauthorized 응답을 하도록 구현한다.
                //                        .authenticationEntryPoint((request, response, authException) ->
                //                                response.setStatus(HttpStatus.UNAUTHORIZED.value())
                //                        )
                //                        // 인증된 사용자가 권한이 없는 리소스에 접근할 때 호출되는 핸들러이다.
                //                        // 403 Forbidden 응답을 하도록 구현한다.
                //                        .accessDeniedHandler((request, response, accessDeniedException) ->
                //                                response.setStatus(HttpStatus.FORBIDDEN.value())
                //                        )
                //                )
                //                구현 클래스를 작성해서 JSON 문자열로 응답하도록 구현
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(new AuthenticationEntryPointImpl())
                                .accessDeniedHandler(new AccessDeniedHandlerImpl())
                )
                .authorizeHttpRequests(requests ->
                        requests
                                // Swagger
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**").permitAll()
                                // 로그인 요청 허용
                                // .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/logout").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/auth/login", "/api/v1/auth/refresh").permitAll()
                                // 모든 GET 요청 허용
                                // .requestMatchers(HttpMethod.GET, "/api/v1/**").permitAll()
                                // 모든 DELETE 요청의 경우 관리자만 허용
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/**").hasRole("ADMIN")
                                // 이 외의 모든 요청은 인증이 필요
                                .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}
