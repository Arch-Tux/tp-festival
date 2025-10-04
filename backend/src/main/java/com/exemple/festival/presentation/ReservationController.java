package com.exemple.festival.presentation;

import com.exemple.festival.business.entities.Reservation;
import com.exemple.festival.business.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    // ========== READ OPERATIONS ==========

    /**
     * Récupérer toutes les réservations
     */
    @GetMapping
    public ResponseEntity<List<Reservation>> findAll() {
        List<Reservation> reservations = reservationService.findAll();
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupérer une réservation par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> findById(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.findById(id);
        
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Compter le nombre total de réservations
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = reservationService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Récupérer les réservations d'un concert
     */
    @GetMapping("/concert/{concertId}")
    public ResponseEntity<List<Reservation>> findByConcertId(@PathVariable Long concertId) {
        List<Reservation> reservations = reservationService.findByConcertId(concertId);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupérer les réservations d'un email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<List<Reservation>> findByEmail(@PathVariable String email) {
        List<Reservation> reservations = reservationService.findByEmail(email);
        return ResponseEntity.ok(reservations);
    }

    /**
     * Récupérer une réservation pour un concert et un email
     */
    @GetMapping("/concert/{concertId}/email/{email}")
    public ResponseEntity<Reservation> findByConcertAndEmail(
            @PathVariable Long concertId, 
            @PathVariable String email) {
        Optional<Reservation> reservation = reservationService.findByConcertAndEmail(concertId, email);
        
        if (reservation.isPresent()) {
            return ResponseEntity.ok(reservation.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Compter les réservations pour un concert
     */
    @GetMapping("/concert/{concertId}/count")
    public ResponseEntity<Long> countByConcertId(@PathVariable Long concertId) {
        Long count = reservationService.countByConcertId(concertId);
        return ResponseEntity.ok(count);
    }

    // ========== CREATE OPERATION ==========

    /**
     * Créer une nouvelle réservation
     */
    @PostMapping
    public ResponseEntity<Reservation> create(@RequestBody Reservation reservation) {
        try {
            // S'assurer que l'ID est null pour une création
            reservation.setId(null);
            Reservation savedReservation = reservationService.save(reservation);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== UPDATE OPERATION ==========

    /**
     * Modifier une réservation existante
     */
    @PutMapping("/{id}")
    public ResponseEntity<Reservation> update(@PathVariable Long id, @RequestBody Reservation reservation) {
        try {
            // Vérifier que la réservation existe
            if (!reservationService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            // S'assurer que l'ID correspond
            reservation.setId(id);
            Reservation updatedReservation = reservationService.save(reservation);
            return ResponseEntity.ok(updatedReservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Supprimer une réservation par ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!reservationService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer toutes les réservations d'un concert
     */
    @DeleteMapping("/concert/{concertId}")
    public ResponseEntity<Void> deleteByConcertId(@PathVariable Long concertId) {
        reservationService.deleteByConcertId(concertId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer toutes les réservations et reset des IDs
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        reservationService.deleteAllAndResetIds();
        return ResponseEntity.noContent().build();
    }
}
