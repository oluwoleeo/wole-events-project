package com.wole.eventsapp.security.manager;

import com.wole.eventsapp.domain.service.UserService;
import com.wole.eventsapp.infrastructure.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {
    private final UserService _userService;
    private final BCryptPasswordEncoder _bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(authentication.getName());
        User user = _userService.getUserByEmail(authentication.getName());

        boolean isCorrect = _bCryptPasswordEncoder.matches(authentication.getCredentials().toString(), user.getPassword());

        if (!isCorrect){
            throw new BadCredentialsException("Wrong password");
        }

        return new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
    }
}
