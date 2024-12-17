package com.wole.eventsapp.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message="name is required")
    @Size(max = 100, message="name is limited to 100 characters")
    private String name;

    @Email(message="invalid email")
    private String email;

    @Size(min = 8, message="password must be a least 8 characters")
    private String password;

    @Column(name="created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name="updated_at")
    private Instant updatedAt;

    @Column(name="deleted_at")
    private Instant deletedAt;
}
