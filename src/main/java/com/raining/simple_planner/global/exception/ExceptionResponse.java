package com.raining.simple_planner.global.exception;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ExceptionResponse {
    private String code;
    private String message;
    private List<FieldError> errors;

    private ExceptionResponse(ExceptionCode ExceptionCode, List<FieldError> fieldErrors) {
        this.code = ExceptionCode.getCode();
        this.message = ExceptionCode.getMessage();
        this.errors = fieldErrors;
    }

    private ExceptionResponse(ExceptionCode ExceptionCode) {
        this.code = ExceptionCode.getCode();
        this.message = ExceptionCode.getMessage();
        this.errors = new ArrayList<>();
    }

    public static ExceptionResponse of(final ExceptionCode ExceptionCode, final BindingResult bindingResult) {
        return new ExceptionResponse(ExceptionCode, FieldError.of(bindingResult));
    }

    @Getter
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String value;
        private String reason;

    public static List<FieldError> of(final BindingResult bindingResult) {
        final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
        return fieldErrors.stream()
            .map(error ->
                new FieldError(error.getField(),
                error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
                error.getDefaultMessage()))
            .collect(Collectors.toList());
        }
    }
}

