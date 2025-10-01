package com.exemple.festival.business.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "concerts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // relation Many-to-One avec Artist
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_concert_artist"))
    private Artist artist;

    @Column(name = "starts_at", nullable = false)
    private String startsAt; // ISO datetime → peut être LocalDateTime si tu préfères

    @Column(nullable = false)
    private Integer capacity;
}
