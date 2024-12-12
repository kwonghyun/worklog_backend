package com.example.worklog.config;

import com.example.worklog.jwt.JwtValidationFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final JwtValidationFilter jwtValidationFilter;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/static/**", "/js/**", "/css/**"
                        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        config.setAllowedOrigins(Arrays.asList(
                                "http://localhost:8100",
                                "https://today-worklog.vercel.app",
                                "https://today.worklog.shop",
                                "http://localhost:63342"
                        ));
                        config.setAllowedMethods(
                                List.of(
                                    HttpMethod.GET.name(),
                                    HttpMethod.HEAD.name(),
                                    HttpMethod.POST.name(),
                                    HttpMethod.PUT.name(),
                                    HttpMethod.DELETE.name(),
                                    HttpMethod.OPTIONS.name(),
                                    HttpMethod.PATCH.name()
                                )
                        );
                        config.setAllowCredentials(true);
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        config.setMaxAge(3600L); //1시간
                        return config;
                    }
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authHttp -> authHttp
                        .requestMatchers(
                                HttpMethod.GET,
                                "/", "/connect"
                                )
                        .permitAll()
                        .requestMatchers(
                                HttpMethod.POST,
                                "/users",
                                "/users/login"
                        )
                        .anonymous()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/users/email/check",
                                "/users/username/check",
                                "/users/password/check",
                                "/users/password-check/check"
                        ).anonymous()
                        .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtValidationFilter, AuthorizationFilter.class)
        ;
        return http.build();
    }

}
