package com.exemple.festival.Concert.application.usecase;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Concert.domain.entities.Concert;
import com.exemple.festival.Concert.infrastructure.repositories.ConcertRepository;

/**
 * Use case pour récupérer tous les concerts
 */
@Service
@Transactional(readOnly = true)
public class GetAllConcerts {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    /**
     * Récupérer tous les concerts
     * 
     * @return Liste de tous les concerts
     */
    public List<Concert> execute() {
        return concertRepository.findAll();
    }
}