package com.exemple.festival.data.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.business.entities.Concert;

@Repository
public interface ConcertRepository extends JpaRepository<Concert, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE concerts_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
    
    List<Concert> findByArtistId(Long artistId);
    
    List<Concert> findByTitleContainingIgnoreCase(String title);
    
    List<Concert> findByDateAfter(LocalDateTime date);
    
    List<Concert> findByDateBefore(LocalDateTime date);
    
    List<Concert> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT c FROM Concert c WHERE c.availableTickets > 0")
    List<Concert> findConcertsWithAvailableTickets();
    
    @Query("SELECT c FROM Concert c WHERE c.artist.id = :artistId AND c.availableTickets > 0")
    List<Concert> findAvailableConcertsByArtist(@Param("artistId") Long artistId);
}
