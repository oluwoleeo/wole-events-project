package com.wole.eventsapp.security;

import com.wole.eventsapp.domain.service.UserService;
import com.wole.eventsapp.security.filter.AuthenticationFilter;
import com.wole.eventsapp.security.filter.ExceptionHandlerFilter;
import com.wole.eventsapp.security.filter.JWTAuthorizationFilter;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import com.wole.eventsapp.utils.JwtUtils;

@Configuration
@AllArgsConstructor
public class SecurityConfig {

    private final AuthenticationManager _authenticationManager;
    private final JwtUtils _jwtUtils;
    private final UserService _userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(_authenticationManager, _jwtUtils, _userService);
        authenticationFilter.setUsernameParameter("email");
        authenticationFilter.setFilterProcessesUrl("/auth");

        return http
                .csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable())
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(HttpMethod.POST,"/users/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/events/**").permitAll()
                                .requestMatchers(HttpMethod.GET, new String[]{"/swagger-ui.html", "/api-docs", "/swagger/**"}).permitAll()
                                .anyRequest().authenticated())
                .addFilterBefore(new ExceptionHandlerFilter(), AuthenticationFilter.class)
                .addFilter(authenticationFilter)
                .addFilterAfter(new JWTAuthorizationFilter(_jwtUtils), AuthenticationFilter.class)
                .sessionManagement(session -> session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //.oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer -> httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults()))
                .build();
    }
}
