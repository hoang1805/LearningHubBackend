package com.example.learninghubbackend.commons;

import com.example.learninghubbackend.commons.exceptions.CustomException;
import com.example.learninghubbackend.dtos.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.CompletionException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleCustomException(CustomException e) {
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.error(e.getMessage(), e.getData(), e.getCode())
        );
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<Object> handleCompletionException(CompletionException e) {
        Throwable cause = e.getCause();
        if (cause instanceof CustomException) {
            return handleCustomException((CustomException) cause);
        }

        if (cause instanceof InsufficientAuthenticationException) {
            return handleInsufficientAuthenticationException((InsufficientAuthenticationException) cause);
        }

        return handleException(new Exception(cause));
    }

    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ResponseEntity<Object> handleInsufficientAuthenticationException(InsufficientAuthenticationException e) {
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.error(e.getMessage(), HttpStatus.UNAUTHORIZED.value())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.OK).body(
                BaseResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value())
        );
    }
}
