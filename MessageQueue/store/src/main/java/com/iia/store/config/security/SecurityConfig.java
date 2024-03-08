package com.iia.store.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUserAuthenticationFilter jwtUserAuthenticationFilter;
    private final JwtStoreAuthenticationFilter jwtStoreAuthenticationFilter;
    private final ExpiredJwtExceptionFilter expiredJwtExceptionFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling((exceptionConfig) ->
                        exceptionConfig.authenticationEntryPoint(customAuthenticationEntryPoint).accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(jwtStoreAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtUserAuthenticationFilter, JwtStoreAuthenticationFilter.class)
                .addFilterBefore(expiredJwtExceptionFilter, JwtUserAuthenticationFilter.class)
                .authorizeHttpRequests(authorize ->
                        authorize
                        .requestMatchers(HttpMethod.POST, "/sign-in", "/sign-up","/refresh-token").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/members/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/stores","/stores/**").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/stores/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.GET, "/stores/sign-in").hasAnyRole("USER")
                        .requestMatchers(HttpMethod.POST, "/products").hasAnyRole("NORMAL","SPECIAL")
                        .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("NORMAL","SPECIAL")
                        .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole("NORMAL","SPECIAL")
                        .requestMatchers(HttpMethod.GET).permitAll()
                        .anyRequest().hasAnyRole("ADMIN"));
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
