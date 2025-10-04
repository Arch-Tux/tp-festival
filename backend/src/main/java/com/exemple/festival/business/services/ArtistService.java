package com.exemple.festival.business.services;

import com.exemple.festival.business.entities.Artist;
import com.exemple.festival.data.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ArtistService {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    // ========== OPÉRATIONS CRUD ==========
    
    /**
     * Créer ou modifier un artiste
     */
    public Artist save(Artist artist) {
        if (artist.getName() == null || artist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");
        }
        
        // Vérifier les doublons lors de la création
        if (artist.getId() == null && artistRepository.existsByName(artist.getName())) {
            throw new IllegalArgumentException("Un artiste avec ce nom existe déjà");
        }
        
        return artistRepository.save(artist);
    }
    
    /**
     * Trouver un artiste par ID
     */
    public Optional<Artist> findById(Long id) {
        return artistRepository.findById(id);
    }
    
    /**
     * Trouver tous les artistes
     */
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }
    
    /**
     * Supprimer un artiste par ID
     */
    public void deleteById(Long id) {
        artistRepository.deleteById(id);
    }
    
    /**
     * Supprimer tous les artistes et reset des IDs
     */
    public void deleteAllAndResetIds() {
        artistRepository.deleteAll();
        artistRepository.resetIdSequence();
    }
    
    // ========== MÉTHODES SUPPLÉMENTAIRES ==========
    
    /**
     * Trouver un artiste par nom exact
     */
    public Optional<Artist> findByName(String name) {
        return artistRepository.findByName(name);
    }
    
    /**
     * Rechercher des artistes par mot-clé dans le nom
     */
    public List<Artist> searchByName(String keyword) {
        return artistRepository.findByNameContainingIgnoreCase(keyword);
    }
    
    /**
     * Vérifier si un artiste existe par nom
     */
    public boolean existsByName(String name) {
        return artistRepository.existsByName(name);
    }
    
    /**
     * Compter le nombre total d'artistes
     */
    public long count() {
        return artistRepository.count();
    }
    
    /**
     * Vérifier si un artiste existe par ID
     */
    public boolean existsById(Long id) {
        return artistRepository.existsById(id);
    }
}
