package com.exemple.festival.port.input.concert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.port.output.ConcertRepository;

/**
 * Use case pour supprimer tous les concerts (opération dangereuse)
 */
@Service
@Transactional
public class DeleteAllConcerts {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    /**
     * Supprimer tous les concerts et reset des IDs
     * ⚠️ ATTENTION: Cette opération est irréversible
     */
    public void execute() {
        concertRepository.deleteAll();
        concertRepository.resetIdSequence();
    }
    
    /**
     * Supprimer tous les concerts sans reset des IDs
     * ⚠️ ATTENTION: Cette opération est irréversible
     */
    public void executeWithoutReset() {
        concertRepository.deleteAll();
    }
}