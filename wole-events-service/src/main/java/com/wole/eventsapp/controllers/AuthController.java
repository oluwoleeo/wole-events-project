package com.wole.eventsapp.controllers;


import com.wole.eventsapp.api.AuthApi;
import com.wole.eventsapp.model.Credentials;
import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;

public class AuthController implements AuthApi {
    @Override
    public ResponseEntity<Void> authPost(@Valid Credentials credentials) {
        return AuthApi.super.authPost(credentials);
    }
}
