package com.exemple.festival.presentation;

import com.exemple.festival.business.entities.Reservation;
import com.exemple.festival.business.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

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
}
