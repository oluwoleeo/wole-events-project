package com.wole.eventsapp.domain.service;

import com.wole.eventsapp.exceptions.EntityNotFoundException;
import com.wole.eventsapp.infrastructure.mapper.UserMapper;
import com.wole.eventsapp.infrastructure.repository.UserRepository;
import com.wole.eventsapp.infrastructure.entity.User;
import com.wole.eventsapp.model.UserCreatedDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository _userRepository;
    private final BCryptPasswordEncoder _bCryptPasswordEncoder;


    @Override
    public UserCreatedDTO createUser(com.wole.eventsapp.model.User user) {
        boolean userExists = userWithEmailExists(user.getEmail().trim());

        UserCreatedDTO response = new UserCreatedDTO();

        if (!userExists){
            user.setPassword(_bCryptPasswordEncoder.encode(user.getPassword()));
            User entity = UserMapper.mapToUserEntity(user);
            User createdUser = _userRepository.saveAndFlush(entity);

            response.userId(createdUser.getId());
        }

        return response;
    }

    @Override
    public User getUserByEmail(String email) {
        Optional<User> user = _userRepository.findByEmail(email)
                .filter(u -> u.getDeletedAt() == null);

        return getUser(user);
    }

    private User getUser(Optional<User> user){
        if (user.isPresent()){
            return user.get();
        }

        throw new EntityNotFoundException(User.class);
    }

    private boolean userWithEmailExists(String email){
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("email", ExampleMatcher.GenericPropertyMatcher::ignoreCase);

        User probe = User.builder()
                .email(email)
                .build();

        Example<User> example = Example.of(probe, exampleMatcher);

        return _userRepository.exists(example);
    }
}
