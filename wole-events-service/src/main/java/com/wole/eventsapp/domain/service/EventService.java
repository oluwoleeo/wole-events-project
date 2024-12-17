package com.wole.eventsapp.domain.service;

import com.wole.eventsapp.model.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public interface EventService {
    EventsPost201Response createEvent(EventRequestDTO eventRequestDTO, String userEmail);
    List<EventResponseDTO> getEventsByFilters(String name, String category, OffsetDateTime startDate, OffsetDateTime endDate);
    TicketResponseDTO makeReservation(Long eventId, TicketRequest ticketRequest, String userEmail);
    List<EventResponseDTO> getEventsBookedByUser(String userEmail);
    DeleteReservationResponse deleteReservation(UUID reservationId, String userEmail);
}
