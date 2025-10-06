
package com.exemple.festival.Artist.infrastructure.controllers;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exemple.festival.Artist.application.usecase.ArtistService;
import com.exemple.festival.Artist.domain.entities.Artist;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

    // ========== READ OPERATIONS ==========

    /**
     * Récupérer tous les artistes
     */
    @GetMapping
    public ResponseEntity<List<Artist>> findAll() {
        List<Artist> artists = artistService.findAll();
        return ResponseEntity.ok(artists);
    }

    /**
     * Récupérer un artiste par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable Long id) {
        Optional<Artist> artist = artistService.findById(id);
        
        if (artist.isPresent()) {
            return ResponseEntity.ok(artist.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Compter le nombre total d'artistes
     */
    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        long count = artistService.count();
        return ResponseEntity.ok(count);
    }

    /**
     * Rechercher des artistes par nom (mot-clé)
     */
    @GetMapping("/search")
    public ResponseEntity<List<Artist>> searchByName(@RequestParam String keyword) {
        List<Artist> artists = artistService.searchByName(keyword);
        return ResponseEntity.ok(artists);
    }

    // ========== CREATE OPERATION ==========

    /**
     * Créer un nouvel artiste
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Artist artist) {
        try {
            // S'assurer que l'ID est null pour une création
            artist.setId(null);
            Artist savedArtist = artistService.save(artist);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== UPDATE OPERATION ==========

    /**
     * Modifier un artiste existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Artist artist) {
        try {
            // Vérifier que l'artiste existe
            if (!artistService.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            
            // S'assurer que l'ID correspond
            artist.setId(id);
            Artist updatedArtist = artistService.save(artist);
            return ResponseEntity.ok(updatedArtist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ========== DELETE OPERATIONS ==========

    /**
     * Supprimer un artiste par ID
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        if (!artistService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        
        artistService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer tous les artistes et reset des IDs
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        artistService.deleteAllAndResetIds();
        return ResponseEntity.noContent().build();
    }
}
