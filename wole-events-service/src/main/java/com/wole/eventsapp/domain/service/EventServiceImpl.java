package com.wole.eventsapp.domain.service;

import com.wole.eventsapp.exceptions.EntityNotFoundException;
import com.wole.eventsapp.exceptions.InvalidOperationException;
import com.wole.eventsapp.infrastructure.entity.Category;
import com.wole.eventsapp.infrastructure.entity.Event;
import com.wole.eventsapp.infrastructure.entity.Reservation;
import com.wole.eventsapp.infrastructure.entity.User;
import com.wole.eventsapp.infrastructure.mapper.EventMapper;
import com.wole.eventsapp.infrastructure.repository.CategoryRepository;
import com.wole.eventsapp.infrastructure.repository.EventRepository;
import com.wole.eventsapp.infrastructure.repository.ReservationRepository;
import com.wole.eventsapp.model.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService{

    private final EventRepository _eventRepository;
    private final CategoryRepository _categoryRepository;
    private final ReservationRepository _reservationRepository;
    private final UserService _userService;

    @Override
    public EventsPost201Response createEvent(EventRequestDTO eventRequestDTO, String userEmail) {
        Category category = _categoryRepository.findByName(eventRequestDTO.getCategory().toString());
        User currentUser = _userService.getUserByEmail(userEmail);

        Event mappedEvent = EventMapper.mapToEventEntity(eventRequestDTO, category, currentUser);
        Event createdEvent = _eventRepository.saveAndFlush(mappedEvent);

        EventsPost201Response response = new EventsPost201Response();

        response.eventId(createdEvent.getId().longValue());

        return response;
    }

    @Override
    public List<EventResponseDTO> getEventsByFilters(String name, String category, OffsetDateTime startDate, OffsetDateTime endDate) {
        List<Event> events;

        // A huge mess. Will refactor if there is time to.

        if (name != null && category != null && startDate != null && endDate != null){
            events = _eventRepository.getByAllFilters(name, category, startDate, endDate);
        }
        else{
            if (name != null){
                events = _eventRepository.findByName(name);

                if (category != null){
                    events = events.stream()
                            .filter(e -> e.getCategory().getName().equals(category))
                            .toList();
                }

                if (startDate != null){
                    events = events.stream()
                            .filter(e -> e.getEventDate().isEqual(startDate) || e.getEventDate().isAfter(startDate))
                            .toList();
                }

                if (endDate != null){
                    events = events.stream()
                            .filter(e -> e.getEventDate().isBefore(endDate) || e.getEventDate().isEqual(endDate))
                            .toList();
                }
            }
            else{
                if (category != null){
                    Category categoryEntity = _categoryRepository.findByName(category);

                    events = _eventRepository.findByCategory(categoryEntity);

                    if (startDate != null){
                        events = events.stream()
                                .filter(e -> e.getEventDate().isEqual(startDate) || e.getEventDate().isAfter(startDate))
                                .toList();
                    }

                    if (endDate != null){
                        events = events.stream()
                                .filter(e -> e.getEventDate().isBefore(endDate) || e.getEventDate().isEqual(endDate))
                                .toList();
                    }
                }
                else {
                    if (startDate != null && endDate != null){
                        events = _eventRepository.getByDateRange(startDate, endDate);
                    }
                    else if (startDate != null) {
                        events = _eventRepository.getByStartDate(startDate);
                    }
                    else{
                        events = _eventRepository.getByEndDate(endDate);
                    }
                }
            }
        }

        if (events != null){
            events = events.stream()
                    .filter(e -> e.getDeletedAt() == null)
                    .toList();
        }

        return EventMapper.mapToEventResponseDTOs(events);
    }

    @Override
    @Transactional
    public TicketResponseDTO makeReservation(Long eventId, TicketRequest ticketRequest, String userEmail) {
        Optional<Event> event = _eventRepository.findById(eventId.intValue())
                .filter(e -> e.getDeletedAt() == null);

        if (event.isEmpty()){
            throw new EntityNotFoundException(Event.class);
        }

        User currentUser = _userService.getUserByEmail(userEmail);
        UUID generatedUUID = generateUUID();
        List<Reservation> reservations = generateReservations(ticketRequest.getAttendeesCount(), event.get(),
                currentUser, generatedUUID);

        _reservationRepository.saveAllAndFlush(reservations);
        updateEventWithReservation(event.get(), reservations.size(), "booking");
        List<Ticket> tickets = new ArrayList<>(reservations.size());

        for (Reservation reservation: reservations){
            tickets.add(new Ticket().id(reservation.getTicketNumber()));
        }

        TicketResponseDTO ticketResponseDTO = new TicketResponseDTO(reservations.size());
        ticketResponseDTO.setReservationId(generateUUID());
        ticketResponseDTO.setTickets(tickets);

        return ticketResponseDTO;
    }

    @Override
    public List<EventResponseDTO> getEventsBookedByUser(String userEmail) {
        User user = _userService.getUserByEmail(userEmail);

        List<Reservation> reservations = _reservationRepository.findByUser(user);

        List<Event> events = reservations.stream()
                .filter(f -> f.getDeletedAt() == null)
                .map(r -> r.getEvent())
                .distinct()
                .filter(e -> e.getDeletedAt() == null)
                .toList();

        return EventMapper.mapToEventResponseDTOs(events);
    }

    @Override
    @Transactional
    public DeleteReservationResponse deleteReservation(UUID reservationId, String userEmail) {
        User user = _userService.getUserByEmail(userEmail);
        List<Reservation> reservations = _reservationRepository.findByReservationId(reservationId)
                .stream().filter(r -> r.getDeletedAt() == null)
                .toList();

        if (reservations.isEmpty()){
            throw new EntityNotFoundException(Reservation.class);
        }

        if (!reservations.get(0).getUser().getEmail().equals(userEmail)){
            throw new InvalidOperationException("You cannot delete a reservation not created by you");
        }

        Instant now = Instant.now();
        reservations.forEach(r -> {
            r.setUpdatedAt(now);
            r.setDeletedAt(now);
        });

        _reservationRepository.saveAllAndFlush(reservations);
        updateEventWithReservation(reservations.get(0).getEvent(), reservations.size(), "cancellation");

        return new DeleteReservationResponse().id(reservationId);
    }

    @Transactional
    private void updateEventWithReservation(Event event, int numberOfReservations, String operation){
        if (operation.equals("booking")){
            event.setBookedTicketsCount(event.getBookedTicketsCount() + numberOfReservations);
            event.setTotalBookings(event.getTotalBookings() + numberOfReservations);
        }

        if (operation.equals("cancellation")){
            event.setBookedTicketsCount(event.getBookedTicketsCount() - numberOfReservations);
        }

        event.setUpdatedAt(Instant.now());

        _eventRepository.saveAndFlush(event);
    }

    private List<Reservation> generateReservations(int count, Event event, User user, UUID reservationId){
        List<Reservation> reservations = new ArrayList<>(count);

        int attendees = event.getAvailableAttendeesCount();
        int booked = event.getBookedTicketsCount();
        int totalBookings = event.getTotalBookings();

        int ticketsAvailable = attendees - booked;

        int i = 0;
        while (i < count && ticketsAvailable >= count - i){
            totalBookings = totalBookings+1;

            Reservation reservation = Reservation.builder()
                    .reservationId(reservationId)
                    .event(event)
                    .user(user)
                    .ticketNumber(totalBookings)
                    .build();
            reservations.add(reservation);

            i++;
            ticketsAvailable--;
        }

        return reservations;
    }

    private UUID generateUUID(){
        return UUID.randomUUID();
    }
}
