package com.exemple.festival.port.input.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.artist.Artist;
import com.exemple.festival.port.output.ArtistRepository;

/**
 * Use case pour créer un nouvel artiste
 */
@Service
@Transactional
public class CreateArtist {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Créer un nouvel artiste avec validation
     * 
     * @param artist L'artiste à créer
     * @return L'artiste créé avec son ID généré
     * @throws IllegalArgumentException si le nom est vide ou si l'artiste existe déjà
     */
    public Artist execute(Artist artist) {
        validateArtistData(artist);
        validateUniqueArtistName(artist.getName());
        
        return artistRepository.save(artist);
    }
    
    private void validateArtistData(Artist artist) {
        if (artist.getName() == null || artist.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide");
        }
    }
    
    private void validateUniqueArtistName(String name) {
        if (artistRepository.existsByName(name)) {
            throw new IllegalArgumentException("Un artiste avec ce nom existe déjà");
        }
    }
}
