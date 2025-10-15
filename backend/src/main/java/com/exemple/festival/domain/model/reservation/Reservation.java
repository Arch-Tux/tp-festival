package com.exemple.festival.domain.model.reservation;

import java.time.LocalDateTime;

import com.exemple.festival.domain.model.concert.Concert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
