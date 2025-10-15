package com.exemple.festival.port.input.concert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.concert.Concert;
import com.exemple.festival.port.output.ConcertRepository;

/**
 * Use case pour récupérer les concerts d'un artiste
 */
@Service
@Transactional(readOnly = true)
public class GetConcertsByArtist {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    /**
     * Trouver les concerts d'un artiste
     * 
     * @param artistId L'ID de l'artiste
     * @return Liste des concerts de l'artiste
     * @throws IllegalArgumentException si l'ID de l'artiste est null
     */
    public List<Concert> execute(Long artistId) {
        validateArtistId(artistId);
        return concertRepository.findByArtistId(artistId);
    }
    
    private void validateArtistId(Long artistId) {
        if (artistId == null) {
            throw new IllegalArgumentException("L'ID de l'artiste ne peut pas être null");
        }
    }
}