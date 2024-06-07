package io.elice.shoppingmall.security;

import io.elice.shoppingmall.member.entity.Member;
import io.elice.shoppingmall.member.service.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        System.out.println("[Log] JwtTokenFilter.doFilterInternal 실행");

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorizationHeader == null){
            System.out.println("[Log] authorizationHeader 이 없음");

            if(request.getCookies() == null) {
                System.out.println("[Log] HttpServletResponse에 쿠키가 없음");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("[Log] 쿠키의 jwtToken 확인 중");

            Cookie jwtTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("jwtToken"))
                .findFirst()
                .orElse(null);

            if(jwtTokenCookie == null){
                System.out.println("[Log] 쿠키에 jwtToken이 없음");
                filterChain.doFilter(request, response);
                return;
            }

            System.out.println("[Log] 쿠키에 jwtToken이 존재함");

            String jwtToken = jwtTokenCookie.getValue();
            authorizationHeader = "Bearer " + jwtToken;

            System.out.println("[Log] authorizationHeader 생성");
        }

        if(!authorizationHeader.startsWith("Bearer ")){
            System.out.println("[Log] authorizationHeader이 \"Bearer \" 로 시작하지 않음");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("[Log] authorizationHeader의 Bearer 확인");

        String token = authorizationHeader.split(" ")[1];

        if(JwtTokenUtil.isExpired(token, secretKey)){
            System.out.println("[Log] 토큰의 기한 만료");
            
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("[Log] 토큰의 username 확인");

        String username = JwtTokenUtil.getUsername(token, secretKey);
        Member member = memberService.findByUsername(username).orElse(null);

        if(member == null){
            System.out.println("[Log] 토큰의 username 과 동일한 member 없음");
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("[Log] UsernamePasswordAuthenticationToken 발급");

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                member.getUsername(), null, List.of(new SimpleGrantedAuthority(member.getAdmin())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        System.out.println("[Log] 권한 부여");

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }
}
