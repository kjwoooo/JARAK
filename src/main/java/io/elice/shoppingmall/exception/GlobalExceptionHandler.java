package io.elice.shoppingmall.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleException(CustomException e){
        ErrorResponse response = new ErrorResponse(e.getCode().getState(), e.getCode().getMessage());
        log.error("MESSAGE: {}, ERROR CODE: {}", response.getMessage(), response.getState());

        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getState()));
    }
}
