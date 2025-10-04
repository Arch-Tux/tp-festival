
package com.exemple.festival.presentation;

import com.exemple.festival.business.entities.Artist;
import com.exemple.festival.business.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private ArtistService artistService;

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
}
