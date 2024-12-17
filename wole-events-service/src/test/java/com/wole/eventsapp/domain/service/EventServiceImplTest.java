package com.wole.eventsapp.domain.service;

import com.wole.eventsapp.infrastructure.entity.Category;
import com.wole.eventsapp.infrastructure.entity.Event;
import com.wole.eventsapp.infrastructure.entity.User;
import com.wole.eventsapp.infrastructure.repository.CategoryRepository;
import com.wole.eventsapp.infrastructure.repository.EventRepository;
import com.wole.eventsapp.infrastructure.repository.ReservationRepository;
import com.wole.eventsapp.infrastructure.repository.UserRepository;
import com.wole.eventsapp.model.EventRequestDTO;
import com.wole.eventsapp.model.EventsPost201Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceImplTest {
    @Mock
    private EventRepository _eventRepository;

    @Mock
    private CategoryRepository _categoryRepository;

    @Mock
    private ReservationRepository _reservationRepository;

    @Mock
    private UserRepository _userRepository;

    @Mock
    private BCryptPasswordEncoder _bCryptPasswordEncoder;

    @InjectMocks
    private UserService _userService = new UserServiceImpl(_userRepository, _bCryptPasswordEncoder);

    @InjectMocks
    private EventServiceImpl _eventService;

    @BeforeAll
    static void setup(){

    }

    @Test
    @DisplayName("test_event_created")
    void testEventCreated(){
        EventRequestDTO eventRequestDTO = new EventRequestDTO()
                .availableAttendeesCount(2)
                .date(OffsetDateTime.now())
                .category(com.wole.eventsapp.model.Category.CONCERT)
                .name("An event")
                .description("The big one!");

        Category category = Category.builder()
                .name(com.wole.eventsapp.model.Category.CONCERT.toString())
                .build();
        User user = User.builder()
                .name("abc")
                .email("wsmsmd@g.com")
                .password("msmd")
                .createdAt(Instant.now())
                .build();

        Event e = Event.builder()
                .id(1)
                .name("Event 1")
                .availableAttendeesCount(20)
                .build();

        when(_categoryRepository.findByName(any(String.class))).thenReturn(category);
        when(_userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(user));
        when(_eventRepository.saveAndFlush(any(Event.class))).thenReturn(e);

        EventsPost201Response response = _eventService.createEvent(eventRequestDTO, user.getEmail());

        verify(_categoryRepository, times(1)).findByName(any(String.class));
    }
}
