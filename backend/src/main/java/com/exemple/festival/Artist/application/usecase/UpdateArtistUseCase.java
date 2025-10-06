package com.exemple.festival.Artist.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Artist.domain.entities.Artist;
import com.exemple.festival.Artist.infrastructure.repositories.ArtistRepository;

/**
 * Use case pour modifier un artiste existant
 */
@Service
@Transactional
public class UpdateArtistUseCase {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Modifier un artiste existant
     * 
     * @param artist L'artiste à modifier (doit avoir un ID)
     * @return L'artiste modifié
     * @throws IllegalArgumentException si le nom est vide ou si l'ID n'existe pas
     */
    public Artist execute(Artist artist) {
        validateArtistData(artist);
        validateArtistExists(artist.getId());
        
        return artistRepository.save(artist);
    }
    
    private void validateArtistData(Artist artist) {
        if (artist.getId() == null) {
            throw new IllegalArgumentException("L'ID de l'artiste est requis pour la modification");
        }
        
        if (artist.getName() == null || artist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");
        }
    }
    
    private void validateArtistExists(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new IllegalArgumentException("Aucun artiste trouvé avec l'ID: " + id);
        }
    }
}
