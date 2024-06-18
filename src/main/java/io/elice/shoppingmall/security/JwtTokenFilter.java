package io.elice.shoppingmall.security;

import io.elice.shoppingmall.exception.CustomException;
import io.elice.shoppingmall.exception.ErrorCode;
import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenUtil util;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try{
            Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(util.getJWT_COOKIE_NAME()))
                .findFirst()
                .orElse(null);

            if(jwtTokenCookie == null){
                filterChain.doFilter(request, response);
            }

            String jwtToken = jwtTokenCookie.getValue();


            if(util.isExpired(jwtToken)){
                filterChain.doFilter(request, response);
                return;
            }

            String username = util.getUsername(jwtToken);

            Member member = memberService.findByUsername(username);

            User user = new User(username, member.getLoginInfo().getPassword(), List.of(new SimpleGrantedAuthority(member.getAuthority())));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, List.of(new SimpleGrantedAuthority(member.getAuthority())));

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            String newToken = util.tokenExpireExtension(jwtToken);
            Cookie cookie = new Cookie(util.getJWT_COOKIE_NAME(), newToken);
            cookie.setPath("/");
            cookie.setMaxAge(util.getJWT_COOKIE_MAX_AGE());
            response.addCookie(cookie);

        } catch(Exception e){
            util.tokenDestroy(response);
        }

        filterChain.doFilter(request, response);
    }
}
