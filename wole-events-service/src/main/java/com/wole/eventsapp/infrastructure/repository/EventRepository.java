package com.wole.eventsapp.infrastructure.repository;

import com.wole.eventsapp.infrastructure.entity.Category;
import com.wole.eventsapp.infrastructure.entity.Event;
import com.wole.eventsapp.infrastructure.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    @Query(value = "SELECT * FROM events WHERE name = :name AND category_id = (SELECT id FROM event_categories WHERE name = :categoryName) AND event_date >= :startDate AND event_date <= :endDate AND deleted_at IS NULL;", nativeQuery = true)
    List<Event> getByAllFilters(@Param("name") String name, @Param("categoryName") String categoryName, @Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);

    List<Event> findByName(String name);

    List<Event> findByCategory(Category category);

    List<Event> findByUser(User user);

    @Query(value = "SELECT * FROM events WHERE event_date >= :startDate AND event_date <= :endDate AND deleted_at IS NULL;", nativeQuery = true)
    List<Event> getByDateRange(@Param("startDate") OffsetDateTime startDate, @Param("endDate") OffsetDateTime endDate);

    @Query(value = "SELECT * FROM events WHERE event_date >= :startDate AND deleted_at IS NULL;", nativeQuery = true)
    List<Event> getByStartDate(@Param("startDate") OffsetDateTime startDate);

    @Query(value = "SELECT * FROM events WHERE event_date <= :endDate AND deleted_at IS NULL;", nativeQuery = true)
    List<Event> getByEndDate(@Param("endDate") OffsetDateTime endDate);
}
