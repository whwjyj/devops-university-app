package com.beyond.university.common.exception.handler;

import com.beyond.university.common.exception.UniversityException;
import com.beyond.university.common.exception.dto.ApiErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/*
	예외 처리

	1. @RestControllerAdvice
	  - 스프링에서 제공하는 어노테이션으로 @RestController에서 발생하는 예외를 처리할 수 있게 하는 기능을 수행한다.

	2. @ExceptionHandler
	  - 컨트롤러에서 발생하는 예외를 처리하는 메소드를 정의할 때 사용한다.
	  - 메소드에서 처리할 예외는 value 속성으로 지정한다.
	  - 특정 컨트롤러 클래스내에 @ExceptionHandler 어노테이션을 사용하는 메소드를 선언하면
	    해당 클래스에서 발생한 예외만 처리하는 ExceptionHandler 메소드를 정의할 수 있다.
*/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UniversityException.class)
    public ResponseEntity<ApiErrorResponseDto> handleException(UniversityException e) {

        log.error("UniversityException : {}", e.getMessage());

        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(
                e.getStatus().value(),
                e.getType(),
                e.getMessage()
        );

        return new ResponseEntity<>(apiErrorResponseDto, e.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponseDto> handleException(MethodArgumentNotValidException e) {
        StringBuilder errors = new StringBuilder();

        log.error("MethodArgumentNotValidException : {}", e.getMessage());

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            errors
                    .append(fieldError.getField())
                    .append("(")
                    .append(fieldError.getDefaultMessage())
                    .append("), ");
        }

        errors.replace(errors.lastIndexOf(","),  errors.length(), "");

        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                errors.toString()
        );

        return new ResponseEntity<>(apiErrorResponseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDto> handleException(Exception e) {

        log.error("Global Exception : {}", e.getMessage());

        ApiErrorResponseDto apiErrorResponseDto = new ApiErrorResponseDto(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name(),
                e.getMessage()
        );

        return new ResponseEntity<>(apiErrorResponseDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}