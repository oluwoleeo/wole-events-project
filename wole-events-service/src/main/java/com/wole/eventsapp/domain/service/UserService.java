package com.wole.eventsapp.domain.service;

import com.wole.eventsapp.infrastructure.entity.User;
import com.wole.eventsapp.model.UserCreatedDTO;

public interface UserService {
    UserCreatedDTO createUser(com.wole.eventsapp.model.User user);
    User getUserByEmail(String email);
}
