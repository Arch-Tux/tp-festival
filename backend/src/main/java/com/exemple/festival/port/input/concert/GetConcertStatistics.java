package com.exemple.festival.port.input.concert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.port.output.ConcertRepository;

/**
 * Use case pour obtenir des statistiques sur les concerts
 */
@Service
@Transactional(readOnly = true)
public class GetConcertStatistics {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    /**
     * Compter le nombre total de concerts
     * 
     * @return Le nombre total de concerts
     */
    public long getTotalCount() {
        return concertRepository.count();
    }
    
    /**
     * Vérifier si un concert existe par ID
     * 
     * @param id L'ID du concert
     * @return true si le concert existe, false sinon
     * @throws IllegalArgumentException si l'ID est null
     */
    public boolean existsById(Long id) {
        validateId(id);
        return concertRepository.existsById(id);
    }
    
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
    }
}