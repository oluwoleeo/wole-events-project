package com.wole.eventsapp.infrastructure.mapper;

import com.wole.eventsapp.infrastructure.entity.Category;
import com.wole.eventsapp.infrastructure.entity.Event;
import com.wole.eventsapp.infrastructure.entity.User;
import com.wole.eventsapp.model.EventRequestDTO;
import com.wole.eventsapp.model.EventResponseDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMapper {

    public static Event mapToEventEntity(EventRequestDTO eventRequestDTO, Category category, User user){

        return Event.builder()
                .name(eventRequestDTO.getName().trim())
                .eventDate(eventRequestDTO.getDate())
                .availableAttendeesCount(eventRequestDTO.getAvailableAttendeesCount())
                .bookedTicketsCount(0)
                .totalBookings(0)
                .description(eventRequestDTO.getDescription())
                .category(category)
                .user(user)
                .build();
    }

    public static List<EventResponseDTO> mapToEventResponseDTOs(List<Event> events){
        List<EventResponseDTO> eventResponseDTOs = List.of();

        if (events != null){
            eventResponseDTOs = events.stream()
                    .map(
                            event -> new EventResponseDTO()
                                    .id(event.getId().longValue())
                                    .name(event.getName())
                                    .date(event.getEventDate())
                                    .availableAttendeesCount(event.getAvailableAttendeesCount())
                                    .description(event.getDescription())
                                    .category(com.wole.eventsapp.model.Category.fromValue(event.getCategory().getName())))
                    .toList();
        }

        return eventResponseDTOs;
    }
}
