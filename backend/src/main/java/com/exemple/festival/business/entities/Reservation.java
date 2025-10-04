package com.exemple.festival.business.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter 
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reservations",
       uniqueConstraints = {
           @UniqueConstraint(name = "uc_reservation_concert_email", columnNames = {"concert_id", "email"})
       })

public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false, foreignKey = @ForeignKey(name = "fk_reservation_concert"))
    private Concert concert;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "reserved_at", nullable = false)
    private LocalDateTime reservedAt;
}
