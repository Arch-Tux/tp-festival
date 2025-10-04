package com.exemple.festival.presentation;

import com.exemple.festival.business.entities.Concert;
import com.exemple.festival.business.services.ConcertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    /**
     * Récupérer tous les concerts
     */
    @GetMapping
    public ResponseEntity<List<Concert>> findAll() {
        List<Concert> concerts = concertService.findAll();
        return ResponseEntity.ok(concerts);
    }

    /**
     * Récupérer un concert par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Concert> findById(@PathVariable Long id) {
        Optional<Concert> concert = concertService.findById(id);
        
        if (concert.isPresent()) {
            return ResponseEntity.ok(concert.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Compter le nombre total de concerts
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = concertService.count();
        return ResponseEntity.ok(count);
    }
}
