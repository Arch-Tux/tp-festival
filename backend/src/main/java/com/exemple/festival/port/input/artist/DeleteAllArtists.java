package com.exemple.festival.port.input.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.port.output.ArtistRepository;

/**
 * Use case pour supprimer tous les artistes (opération dangereuse)
 */
@Service
@Transactional
public class DeleteAllArtists {
    
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
