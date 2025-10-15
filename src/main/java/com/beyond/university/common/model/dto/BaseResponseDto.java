package com.beyond.university.common.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class BaseResponseDto<T> {
    @Schema(description = "응답 코드", example = "200")
    protected final int code;

    @Schema(description = "응답 메시지", example = "OK")
    protected final String message;

    @Schema(description = "응답 데이터")
    protected final List<T> items;

    public BaseResponseDto(HttpStatus status, T item) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.items = Collections.singletonList(item);
    }

    protected BaseResponseDto(HttpStatus status, List<T> items) {
        this.code = status.value();
        this.message = status.getReasonPhrase();
        this.items = items;
    }
}