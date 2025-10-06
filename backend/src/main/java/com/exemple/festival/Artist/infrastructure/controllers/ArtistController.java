
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
import org.springframework.web.bind.annotation.RestController;

import com.exemple.festival.Artist.application.usecase.CreateArtistUseCase;
import com.exemple.festival.Artist.application.usecase.DeleteAllArtistsUseCase;
import com.exemple.festival.Artist.application.usecase.DeleteArtistUseCase;
import com.exemple.festival.Artist.application.usecase.GetAllArtistsUseCase;
import com.exemple.festival.Artist.application.usecase.GetArtistByIdUseCase;
import com.exemple.festival.Artist.application.usecase.GetArtistStatisticsUseCase;
import com.exemple.festival.Artist.application.usecase.UpdateArtistUseCase;
import com.exemple.festival.Artist.domain.entities.Artist;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {

    @Autowired
    private CreateArtistUseCase createArtistUseCase;
    @Autowired
    private DeleteAllArtistsUseCase deleteAllArtistsUseCase;
    @Autowired
    private DeleteArtistUseCase deleteArtistUseCase;
    @Autowired
    private GetAllArtistsUseCase getAllArtistsUseCase;
    @Autowired
    private GetArtistByIdUseCase getArtistByIdUseCase;
    @Autowired
    private GetArtistStatisticsUseCase getArtistStatisticsUseCase;
    @Autowired
    private UpdateArtistUseCase updateArtistUseCase;

    // ========== READ OPERATIONS ==========

    /**
     * Récupérer tous les artistes
     */
    @GetMapping
    public ResponseEntity<List<Artist>> findAll() {
        List<Artist> artists = getAllArtistsUseCase.execute();
        return ResponseEntity.ok(artists);
    }

    /**
     * Récupérer un artiste par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Artist> findById(@PathVariable Long id) {
        Optional<Artist> artist = getArtistByIdUseCase.execute(id);
        
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
        long count = getArtistStatisticsUseCase.getTotalCount();
        return ResponseEntity.ok(count);
    }


    // ========== CREATE OPERATION ==========

    /**
     * Créer un nouvel artiste
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Artist artist) {
        try {
            artist.setId(null);
            Artist savedArtist = createArtistUseCase.execute(artist);
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
            Optional<Artist> existingArtist = getArtistByIdUseCase.execute(id);
            if (!existingArtist.isPresent()) {
                return ResponseEntity.notFound().build();
            }
            
            // S'assurer que l'ID correspond
            artist.setId(id);
            Artist updatedArtist = updateArtistUseCase.execute(artist);
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
        Optional<Artist> artist = getArtistByIdUseCase.execute(id);
        if (!artist.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        deleteArtistUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Supprimer tous les artistes et reset des IDs
     */
    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll() {
        deleteAllArtistsUseCase.execute();
        return ResponseEntity.noContent().build();
    }
}
