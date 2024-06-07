package io.elice.shoppingmall.security;


import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.service.MemberService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class SecurityConfig{
    @Getter
    private static final String secretKey = "sdagxcs56d4gxc65g48asfafssg4xc685sasadg84a46asd46w8e4684sadggsdg654xc";
    @Getter
    private static final long expireTimeMs = 1000*60*60;
    private final MemberService memberService;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("[Log] SecurityConfig.filterChain 실행");

        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(authorize -> authorize
            .anyRequest().permitAll());

//        http.authorizeHttpRequests(authorize -> authorize
//            .requestMatchers(HttpMethod.POST, "/loginTest").permitAll()
//            .requestMatchers(HttpMethod.POST, "/register").permitAll()
//            .requestMatchers(HttpMethod.GET,"/member/**").hasAnyRole(MemberAuthority.ADMIN.name(), MemberAuthority.USER.name())
//            .requestMatchers(HttpMethod.GET, "/admin").hasRole(MemberAuthority.ADMIN.name())
//        );

        http.addFilterBefore(new JwtTokenFilter(secretKey, memberService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
