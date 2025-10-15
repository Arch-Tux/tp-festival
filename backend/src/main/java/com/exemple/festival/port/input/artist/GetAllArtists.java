package com.exemple.festival.port.input.artist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.artist.Artist;
import com.exemple.festival.port.output.ArtistRepository;

/**
 * Use case pour récupérer tous les artistes
 */
@Service
@Transactional(readOnly = true)
public class GetAllArtists {
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Récupérer tous les artistes
     * 
     * @return Liste de tous les artistes
     */
    public List<Artist> execute() {
        return artistRepository.findAll();
    }
}
