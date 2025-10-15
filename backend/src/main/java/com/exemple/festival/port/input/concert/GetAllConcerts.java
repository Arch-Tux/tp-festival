package com.exemple.festival.port.input.concert;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.concert.Concert;
import com.exemple.festival.port.output.ConcertRepository;

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