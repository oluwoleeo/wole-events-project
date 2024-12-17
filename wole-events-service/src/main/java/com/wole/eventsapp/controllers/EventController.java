package com.wole.eventsapp.controllers;

import com.wole.eventsapp.api.EventsApi;
import com.wole.eventsapp.domain.service.EventService;
import com.wole.eventsapp.model.DeleteReservationResponse;
import com.wole.eventsapp.model.EventRequestDTO;
import com.wole.eventsapp.model.EventResponseDTO;
import com.wole.eventsapp.model.EventsPost201Response;
import com.wole.eventsapp.model.TicketRequest;
import com.wole.eventsapp.model.TicketResponseDTO;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class EventController implements EventsApi {

    private final EventService _eventService;

    @Override
    public ResponseEntity<TicketResponseDTO> eventsEventIdTicketsPost(Long eventId, @Valid TicketRequest ticketRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        TicketResponseDTO response = _eventService.makeReservation(eventId, ticketRequest, authentication.getName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EventResponseDTO>> eventsGet(@Valid String name, @Valid OffsetDateTime startDate, @Valid OffsetDateTime endDate, @Valid String category) {
        List<EventResponseDTO> eventResponseDTOs = _eventService.getEventsByFilters(name, category, startDate, endDate);

        return new ResponseEntity<>(eventResponseDTOs, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<EventsPost201Response> eventsPost(@Valid EventRequestDTO event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        EventsPost201Response response = _eventService.createEvent(event, authentication.getName());

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<List<EventResponseDTO>> eventsBookingsGet() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<EventResponseDTO> eventResponseDTOs = _eventService.getEventsBookedByUser(authentication.getName());

        return new ResponseEntity<>(eventResponseDTOs, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<DeleteReservationResponse> eventsReservationIdCancelPost(UUID reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        DeleteReservationResponse deleteReservationResponse = _eventService.deleteReservation(reservationId, authentication.getName());
        return new ResponseEntity<>(deleteReservationResponse, HttpStatus.OK);
    }
}
