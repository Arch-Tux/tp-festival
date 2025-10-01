package com.exemple.festival.business.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "artists")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PostgreSQL auto-increment
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
