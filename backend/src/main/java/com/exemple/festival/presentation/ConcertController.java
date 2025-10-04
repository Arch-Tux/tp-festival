package com.exemple.festival.presentation;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.festival.business.entities.Concert;
import com.exemple.festival.business.services.ConcertService;

@RestController
@RequestMapping("/api/concerts")
public class ConcertController {

    @Autowired
    private ConcertService concertService;

    // ========== READ OPERATIONS ==========

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

    /**
     * Récupérer les concerts d'un artiste
     */
    @GetMapping("/artist/{artistId}")
    public ResponseEntity<List<Concert>> findByArtistId(@PathVariable Long artistId) {
        List<Concert> concerts = concertService.findByArtistId(artistId);
        return ResponseEntity.ok(concerts);
    }

    // ========== CREATE OPERATION ==========

    /**
     * Créer un nouveau concert
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Concert concert) {
        try {
            // S'assurer que l'ID est null pour une création
            concert.setId(null);
            Concert savedConcert = concertService.save(concert);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConcert);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== UPDATE OPERATION ==========

    /**
     * Modifier un concert existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Concert concert) {
        try {
            // Vérifier que le concert existe
            if (!concertService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            // S'assurer que l'ID correspond
            concert.setId(id);
            Concert updatedConcert = concertService.save(concert);
            return ResponseEntity.ok(updatedConcert);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Supprimer un concert par ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!concertService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        concertService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer tous les concerts et reset des IDs
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        concertService.deleteAllAndResetIds();
        return ResponseEntity.noContent().build();
    }
}
