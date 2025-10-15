package com.exemple.festival.port.output;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.reservation.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE reservations_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();

    List<Reservation> findByConcertId(Long concertId);
    
    List<Reservation> findByEmail(String email);
    
    Optional<Reservation> findByConcertIdAndEmail(Long concertId, String email);
    
    Long countByConcertId(Long concertId);
    
    void deleteByConcertId(Long concertId);
}
