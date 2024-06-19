package io.elice.shoppingmall.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final PrincipalDetailsService principalDetailsService;
    @Getter
    private final String SECRET_KEY = "sdagxcs56d4gxc65g48asfafssg4xc685sasadg84a46asd46w8e4684sadggsdg654xc";
    @Getter
    private final String JWT_COOKIE_NAME = "jwtToken";
    @Getter
    private final long EXPIRE_TIME_MS = 1000 * 60 * 60;
    @Getter
    private final int JWT_COOKIE_MAX_AGE = 60 * 60 * 24;
    @Getter
    private final int JWT_SUM_EXPIRE_TIME_MS = 1000 * 60 * 5;
    private final String JWT_USERNAME = "username";
    private final String JWT_AUTHORITY = "authority";

    public String createToken(String username, String authority){

        Claims claims = Jwts.claims();
        claims.put(JWT_USERNAME, username);
        claims.put(JWT_AUTHORITY, authority);
        long now = System.currentTimeMillis();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME_MS))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String tokenExpireExtension(String token){
        Claims claims = Jwts.claims();
        claims.put(JWT_USERNAME, getUsername(token));
        claims.put(JWT_AUTHORITY, getAuthenticationInToken(token));
        long expireTime = getExpireTime(token);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(getIssuedAtTime(token)))
            .setExpiration(new Date(getExpireTime(token)))
            .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
            .compact();
    }

    public String getUsername(String token) {
        return extractClaims(token).get(JWT_USERNAME).toString();
    }

    public Authentication getAuthentication(String token){
        Claims claims = extractClaims(token);
        UserDetails userDetails = principalDetailsService.loadUserByUsername(claims.get(JWT_USERNAME).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getAuthenticationInToken(String token) {
        return extractClaims(token).get(JWT_AUTHORITY).toString();
    }

    public boolean isExpired(String token) {
        try{
            Date expiredDate = extractClaims(token).getExpiration();
            return expiredDate.before(new Date());
        } catch (Exception e){
            return false;
        }
    }

    public Long getIssuedAtTime(String token){
        return extractClaims(token).getIssuedAt().getTime();
    }

    public Long getExpireTime(String token){
        return extractClaims(token).getExpiration().getTime();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public void tokenDestroy(HttpServletResponse response){
        Cookie cookie = new Cookie(getJWT_COOKIE_NAME(), null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
