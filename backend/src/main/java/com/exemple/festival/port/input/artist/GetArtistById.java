package com.exemple.festival.port.input.artist;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.artist.Artist;
import com.exemple.festival.port.output.ArtistRepository;

/**
 * Use case pour récupérer un artiste par son ID
 */
@Service
@Transactional(readOnly = true)
public class GetArtistById {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Récupérer un artiste par son ID
     * 
     * @param id L'ID de l'artiste à récupérer
     * @return Optional contenant l'artiste s'il existe
     * @throws IllegalArgumentException si l'ID est null
     */
    public Optional<Artist> execute(Long id) {
        validateId(id);
        return artistRepository.findById(id);
    }
    
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
    }
}
