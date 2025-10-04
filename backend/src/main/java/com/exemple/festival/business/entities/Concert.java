package com.exemple.festival.business.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "concerts")
public class Concert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "artist_id", nullable = false, foreignKey = @ForeignKey(name = "fk_concert_artist"))
    private Artist artist;

    @Column(name = "starts_at", nullable = false)
    private String startsAt; 

    @Column(nullable = false)
    private Integer capacity;
}
