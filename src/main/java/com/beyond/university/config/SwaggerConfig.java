package com.beyond.university.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Department Service API Test",
                description = "학과 관련 API 테스트 문서",
                version = "v1.0.0"
        )
)
public class SwaggerConfig {
        @Bean
        public OpenAPI openAPI() {

                return new OpenAPI()
                        .components(
                                new Components().addSecuritySchemes(
                                        // 보안 스키마의 이름
                                        "bearer-auth",
                                        // 보안 방식 정의
                                        new SecurityScheme()
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                                .type(SecurityScheme.Type.HTTP)
                                )
                        )
                        // 보안 방식 적용
                        .addSecurityItem(
                                new SecurityRequirement().addList("bearer-auth")
                        );
        }
}