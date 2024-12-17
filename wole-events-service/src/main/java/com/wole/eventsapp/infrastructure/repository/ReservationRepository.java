package com.wole.eventsapp.infrastructure.repository;

import com.wole.eventsapp.infrastructure.entity.Reservation;
import com.wole.eventsapp.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
    List<Reservation> findByUser(User user);
    List<Reservation> findByReservationId(UUID reservationId);
}
