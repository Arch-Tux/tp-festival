package com.exemple.festival.Concert.infrastructure.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Concert.domain.entities.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE concerts_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
    
    // Trouver les concerts d'un artiste
    List<Concert> findByArtistId(Long artistId);
    
    // Rechercher par date exacte
    List<Concert> findByStartsAt(Date startsAt);
    
    // Rechercher par date après une date donnée
    List<Concert> findByStartsAtAfter(Date date);
    
    // Rechercher par date avant une date donnée
    List<Concert> findByStartsAtBefore(Date date);
    
    // Rechercher par capacité minimale
    List<Concert> findByCapacityGreaterThanEqual(Integer minCapacity);
}
