package com.wole.eventsapp.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wole.eventsapp.domain.service.UserService;
import com.wole.eventsapp.infrastructure.entity.User;
import com.wole.eventsapp.model.Credentials;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.wole.eventsapp.utils.JwtUtils;

import java.io.IOException;

@AllArgsConstructor
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager _authenticationManager;
    private final JwtUtils _jwtUtils;
    private final UserService _userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            Credentials credentials = new ObjectMapper().readValue(request.getInputStream(), Credentials.class);
            User currentUser = _userService.getUserByEmail(credentials.getEmail());
            Authentication authentication = new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword());

            return _authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String token = _jwtUtils.generateToken(authResult.getName());
        response.addHeader("Authorization", "Bearer " + token);
    }
}
