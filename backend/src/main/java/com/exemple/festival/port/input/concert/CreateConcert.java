package com.exemple.festival.port.input.concert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.concert.Concert;
import com.exemple.festival.port.output.ArtistRepository;
import com.exemple.festival.port.output.ConcertRepository;

/**
 * Use case pour créer un nouveau concert
 */
@Service
@Transactional
public class CreateConcert {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Créer un nouveau concert avec validation
     * 
     * @param concert Le concert à créer
     * @return Le concert créé avec son ID généré
     * @throws IllegalArgumentException si les données sont invalides
     */
    public Concert execute(Concert concert) {
        validateConcertData(concert);
        validateArtistExists(concert.getArtist().getId());
        
        return concertRepository.save(concert);
    }
    
    private void validateConcertData(Concert concert) {
        if (concert.getStartsAt() == null) {
            throw new IllegalArgumentException("La date du concert ne peut pas être nulle");
        }
        
        if (concert.getCapacity() == null || concert.getCapacity() <= 0) {
            throw new IllegalArgumentException("La capacité doit être positive");
        }
        
        if (concert.getArtist() == null || concert.getArtist().getId() == null) {
            throw new IllegalArgumentException("L'artiste est obligatoire");
        }
    }
    
    private void validateArtistExists(Long artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw new IllegalArgumentException("L'artiste spécifié n'existe pas");
        }
    }
}