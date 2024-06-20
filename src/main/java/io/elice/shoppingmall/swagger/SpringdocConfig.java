package io.elice.shoppingmall.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringdocConfig {
    // ui Main 제목, 설명 설정
    // 보안 스키마, 토큰 설정
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("쇼핑몰 API")
                        .version("1.0")
                        .description("의류 쇼핑몰 API 문서"))
                .addSecurityItem(new SecurityRequirement().addList("jwtAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("jwtAuth",
                                new SecurityScheme().
                                        type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    // api 그룹화
    @Bean
    public GroupedOpenApi adminMembersApi() {
        return GroupedOpenApi.builder()
                .group("admin-members")
                .pathsToMatch("/admin/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi membersApi() {
        return GroupedOpenApi.builder()
                .group("members")
                .pathsToMatch("/members/**")
                .build();
    }

    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/login", "/register", "/members-logout", "/unregister", "/oauth2/authorization/google")
                .build();
    }

    @Bean
    public GroupedOpenApi addressesApi() {
        return GroupedOpenApi.builder()
                .group("addresses")
                .pathsToMatch("/addresses/**")
                .build();
    }

    @Bean
    public GroupedOpenApi itemsApi() {
        return GroupedOpenApi.builder()
                .group("items")
                .pathsToMatch("/items/**")
                .build();
    }

    @Bean
    public GroupedOpenApi ordersApi() {
        return GroupedOpenApi.builder()
                .group("orders")
                .pathsToMatch("/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi adminOrdersApi() {
        return GroupedOpenApi.builder()
                .group("admin-orders")
                .pathsToMatch("/admin/orders/**")
                .build();
    }

    @Bean
    public GroupedOpenApi categoriesApi() {
        return GroupedOpenApi.builder()
                .group("categories")
                .pathsToMatch("/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi brandsApi() {
        return GroupedOpenApi.builder()
                .group("brands")
                .pathsToMatch("/brands/**")
                .build();
    }
}
