package com.exemple.festival.Artist.application.usecase;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Artist.infrastructure.repositories.ArtistRepository;

/**
 * Use case pour supprimer tous les artistes (opération dangereuse)
 */
@Service
@Transactional
public class DeleteAllArtistsUseCase {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Supprimer tous les artistes et reset des IDs
     */
    public void execute() {
        artistRepository.deleteAll();
        artistRepository.resetIdSequence();
    }
    
    /**
     * Supprimer tous les artistes sans reset des IDs
     */
    public void deleteAllArtistsWithoutReset() {
        artistRepository.deleteAll();
    }
}
