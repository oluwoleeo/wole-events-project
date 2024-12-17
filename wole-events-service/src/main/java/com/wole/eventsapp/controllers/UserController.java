package com.wole.eventsapp.controllers;

import com.wole.eventsapp.api.UsersApi;
import com.wole.eventsapp.domain.service.UserService;
import com.wole.eventsapp.model.User;
import com.wole.eventsapp.model.UserCreatedDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@AllArgsConstructor
public class UserController implements UsersApi {

    private final UserService _userService;

    @Override
    public ResponseEntity<UserCreatedDTO> usersPost(@Valid User user) {
        UserCreatedDTO createdUser = _userService.createUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
}
