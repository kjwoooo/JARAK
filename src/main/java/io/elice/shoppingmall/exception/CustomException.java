package io.elice.shoppingmall.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private ErrorCode code;

    public CustomException(ErrorCode code){
        this.code = code;
    }
}
