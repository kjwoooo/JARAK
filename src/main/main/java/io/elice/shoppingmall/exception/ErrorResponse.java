package io.elice.shoppingmall.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@Getter
public class ErrorResponse {

    private int state;
    private String message;

    public static ErrorResponse of(final ErrorCode code){
        return new ErrorResponse(code.getState(), code.getMessage());
    }
}
