package com.wole.eventsapp.infrastructure.mapper;

import com.wole.eventsapp.infrastructure.entity.User;

public class UserMapper {
    private UserMapper(){}

    public static User mapToUserEntity(com.wole.eventsapp.model.User userCreationRequest){
        return User.builder()
                .name(userCreationRequest.getName().trim())
                .email(userCreationRequest.getEmail().trim())
                .password(userCreationRequest.getPassword().trim())
                .build();
    }
}
