package com.exemple.festival.Artist.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Artist.infrastructure.repositories.ArtistRepository;

/**
 * Use case pour obtenir des statistiques sur les artistes
 */
@Service
@Transactional(readOnly = true)
public class GetArtistStatistics {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Compter le nombre total d'artistes
     * 
     * @return Le nombre total d'artistes
     */
    public long getTotalCount() {
        return artistRepository.count();
    }
    
    /**
     * Vérifier si un artiste existe par ID
     * 
     * @param id L'ID de l'artiste
     * @return true si l'artiste existe, false sinon
     * @throws IllegalArgumentException si l'ID est null
     */
    public boolean existsById(Long id) {
        validateId(id);
        return artistRepository.existsById(id);
    }
    
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
    }
}
