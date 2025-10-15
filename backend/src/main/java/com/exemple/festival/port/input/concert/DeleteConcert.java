package com.exemple.festival.port.input.concert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.port.output.ConcertRepository;

/**
 * Use case pour supprimer un concert
 */
@Service
@Transactional
public class DeleteConcert {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    /**
     * Supprimer un concert par son ID
     * 
     * @param id L'ID du concert à supprimer
     * @throws IllegalArgumentException si l'ID est null ou si le concert n'existe pas
     */
    public void execute(Long id) {
        validateId(id);
        validateConcertExists(id);
        
        concertRepository.deleteById(id);
    }
    
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
    }
    
    private void validateConcertExists(Long id) {
        if (!concertRepository.existsById(id)) {
            throw new IllegalArgumentException("Aucun concert trouvé avec l'ID: " + id);
        }
    }
}