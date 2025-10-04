
package com.exemple.festival.presentation;

import com.exemple.festival.business.entities.Artist;
import com.exemple.festival.business.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Artist> create(@RequestBody Artist artist) {
        try {
            // S'assurer que l'ID est null pour une création
            artist.setId(null);
            Artist savedArtist = artistService.save(artist);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ========== UPDATE OPERATION ==========

    /**
     * Modifier un artiste existant
     */
    @PutMapping("/{id}")
    public ResponseEntity<Artist> update(@PathVariable Long id, @RequestBody Artist artist) {
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
            return ResponseEntity.badRequest().build();
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
