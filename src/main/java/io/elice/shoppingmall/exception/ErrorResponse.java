package io.elice.shoppingmall.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@AllArgsConstructor
@Getter
@Schema(description = "에러")
public class ErrorResponse {

    @Schema(description = "에러 코드")
    private int state;
    @Schema(description = "에러 메세지")
    private String message;

    public static ErrorResponse of(final ErrorCode code){
        return new ErrorResponse(code.getState(), code.getMessage());
    }
}
