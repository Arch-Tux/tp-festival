package com.exemple.festival.port.input.concert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.concert.Concert;
import com.exemple.festival.port.output.ArtistRepository;
import com.exemple.festival.port.output.ConcertRepository;

/**
 * Use case pour modifier un concert existant
 */
@Service
@Transactional
public class UpdateConcert {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    /**
     * Modifier un concert existant
     * 
     * @param concert Le concert à modifier (doit avoir un ID)
     * @return Le concert modifié
     * @throws IllegalArgumentException si les données sont invalides ou si l'ID n'existe pas
     */
    public Concert execute(Concert concert) {
        validateConcertData(concert);
        validateConcertExists(concert.getId());
        validateArtistExists(concert.getArtist().getId());
        
        return concertRepository.save(concert);
    }
    
    private void validateConcertData(Concert concert) {
        if (concert.getId() == null) {
            throw new IllegalArgumentException("L'ID du concert est requis pour la modification");
        }
        
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
    
    private void validateConcertExists(Long id) {
        if (!concertRepository.existsById(id)) {
            throw new IllegalArgumentException("Aucun concert trouvé avec l'ID: " + id);
        }
    }
    
    private void validateArtistExists(Long artistId) {
        if (!artistRepository.existsById(artistId)) {
            throw new IllegalArgumentException("L'artiste spécifié n'existe pas");
        }
    }
}