package com.raining.simple_planner.global.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleRuntimeException(BusinessException e) {
        final ExceptionCode exceptionCode = e.getExceptionCode();
        final ExceptionResponse response = ExceptionResponse.builder()
            .message(exceptionCode.getMessage())
            .code(exceptionCode.getCode())
            .build();
        log.warn(e.getMessage());
        return ResponseEntity.status(exceptionCode.getStatus()).body(response);
    }

    @ExceptionHandler
    protected ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {
        final ExceptionResponse response = ExceptionResponse.of(ExceptionCode.INVALID_INPUT_VALUE, e.getBindingResult());
        log.warn(e.getMessage());
        return new ResponseEntity<>(response, BAD_REQUEST);
    }
}

