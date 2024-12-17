package com.wole.eventsapp.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.CascadeType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Range;

import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message="name is required")
    @Size(max = 100, message="name is limited to 100 characters")
    private String name;

    @Column(name="event_date")
    private OffsetDateTime eventDate;

    @Column(name="available_attendees_count")
    @Range(max=1000, message="Maximum attendees is 1000")
    private Integer availableAttendeesCount;

    @Column(name="booked_tickets_count")
    @Range(max=1000, message="Maximum attendees is 1000")
    private Integer bookedTicketsCount;

    @Column(name="total_bookings")
    private Integer totalBookings;

    @NotBlank(message="description is required")
    @Size(max = 500, message="description is limited to 500 characters")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="created_by", referencedColumnName = "id")
    private User user;

    @Column(name="created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;

    @Column(name="deleted_at")
    private Instant deletedAt;
}
