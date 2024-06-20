package io.elice.shoppingmall.security;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.sendError(ErrorCode.DO_NOT_HAVE_AUTHORITY.getState(), ErrorCode.DO_NOT_HAVE_AUTHORITY.getMessage());
    }
}
