package io.elice.shoppingmall.security;

import io.elice.shoppingmall.member.MemberAuthority;
import io.elice.shoppingmall.member.service.MemberService;
import io.elice.shoppingmall.security.oautho.OAuth2AuthenticationFailurHandler;
import io.elice.shoppingmall.security.oautho.OAuth2AuthenticationSuccessHandler;
import io.elice.shoppingmall.security.oautho.PrincipalOauth2UserService;
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

    private final JwtTokenUtil util;
    private final MemberService memberService;
    private final PrincipalOauth2UserService principalOauth2UserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
    private final OAuth2AuthenticationFailurHandler oAuth2AuthenticationFailurHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);

        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

//        http.authorizeHttpRequests(authorize -> authorize
//            .anyRequest().permitAll());

        http.authorizeHttpRequests(authorize -> authorize

            //NOTE: 일반회원, 관리자 모두 접근 가능
            .requestMatchers("/unregister", "/logout", "/token-refresh")
                .hasAnyAuthority(MemberAuthority.USER.name(), MemberAuthority.ADMIN.name())
            .requestMatchers(HttpMethod.POST, "/members")
                .hasAuthority(MemberAuthority.USER.name())

            //NOTE: 일반회원만 접근 가능
            .requestMatchers("/members/info", "/addresses/**", "/orders/**", "/carts/**")
                .hasAuthority(MemberAuthority.USER.name())

            //NOTE: 관리자만 접근 가능
            .requestMatchers(HttpMethod.GET, "/admin/**")
                .hasAuthority(MemberAuthority.ADMIN.name())

            .anyRequest().permitAll()
        ).exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);

        http.oauth2Login()
                .userInfoEndpoint()
                    .userService(principalOauth2UserService)
                        .and()
                            .successHandler(oAuth2AuthenticationSuccessHandler);
//                                .and()
//                                    .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);


        http.addFilterBefore(new JwtTokenFilter(util, memberService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new JwtExceptionFilter(), JwtTokenFilter.class);

        return http.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
