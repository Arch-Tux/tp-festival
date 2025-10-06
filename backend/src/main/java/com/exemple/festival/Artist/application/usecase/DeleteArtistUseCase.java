package com.exemple.festival.Artist.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Artist.infrastructure.repositories.ArtistRepository;

/**
 * Use case pour supprimer un artiste
 */
@Service
@Transactional
public class DeleteArtistUseCase {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Supprimer un artiste par son ID
     * 
     * @param id L'ID de l'artiste à supprimer
     * @throws IllegalArgumentException si l'ID est null ou si l'artiste n'existe pas
     */
    public void execute(Long id) {
        validateId(id);
        validateArtistExists(id);
        
        artistRepository.deleteById(id);
    }
    
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
    }
    
    private void validateArtistExists(Long id) {
        if (!artistRepository.existsById(id)) {
            throw new IllegalArgumentException("Aucun artiste trouvé avec l'ID: " + id);
        }
    }
}
