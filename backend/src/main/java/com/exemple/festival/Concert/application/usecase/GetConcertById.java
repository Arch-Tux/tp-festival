package com.exemple.festival.Concert.application.usecase;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Concert.domain.entities.Concert;
import com.exemple.festival.Concert.infrastructure.repositories.ConcertRepository;

/**
 * Use case pour récupérer un concert par son ID
 */
@Service
@Transactional(readOnly = true)
public class GetConcertById {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    /**
     * Récupérer un concert par son ID
     * 
     * @param id L'ID du concert à récupérer
     * @return Optional contenant le concert s'il existe
     * @throws IllegalArgumentException si l'ID est null
     */
    public Optional<Concert> execute(Long id) {
        validateId(id);
        return concertRepository.findById(id);
    }
    
    private void validateId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID ne peut pas être null");
        }
    }
}