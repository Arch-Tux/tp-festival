package com.exemple.festival.business.services;

import com.exemple.festival.business.entities.Concert;
import com.exemple.festival.data.repositories.ConcertRepository;
import com.exemple.festival.data.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ConcertService {
    
    @Autowired
    private ConcertRepository concertRepository;
    
    @Autowired
    private ArtistRepository artistRepository;
    
    // ========== OPÉRATIONS CRUD ==========
    
    /**
     * Créer ou modifier un concert
     */
    public Concert save(Concert concert) {
        // Validations de base
        if (concert.getStartsAt() == null) {
            throw new IllegalArgumentException("La date du concert ne peut pas être nulle");
        }
        
        if (concert.getCapacity() == null || concert.getCapacity() <= 0) {
            throw new IllegalArgumentException("La capacité doit être positive");
        }
        
        if (concert.getArtist() == null || concert.getArtist().getId() == null) {
            throw new IllegalArgumentException("L'artiste est obligatoire");
        }
        
        // Vérifier que l'artiste existe
        if (!artistRepository.existsById(concert.getArtist().getId())) {
            throw new IllegalArgumentException("L'artiste spécifié n'existe pas");
        }
        
        return concertRepository.save(concert);
    }
    
    /**
     * Trouver un concert par ID
     */
    public Optional<Concert> findById(Long id) {
        return concertRepository.findById(id);
    }
    
    /**
     * Trouver tous les concerts
     */
    public List<Concert> findAll() {
        return concertRepository.findAll();
    }
    
    /**
     * Supprimer un concert par ID
     */
    public void deleteById(Long id) {
        concertRepository.deleteById(id);
    }
    
    /**
     * Supprimer tous les concerts et reset des IDs
     */
    public void deleteAllAndResetIds() {
        concertRepository.deleteAll();
        concertRepository.resetIdSequence();
    }
    
    // ========== MÉTHODES SUPPLÉMENTAIRES ==========
    
    /**
     * Trouver les concerts d'un artiste
     */
    public List<Concert> findByArtistId(Long artistId) {
        return concertRepository.findByArtistId(artistId);
    }
    
    /**
     * Compter le nombre total de concerts
     */
    public long count() {
        return concertRepository.count();
    }
    
    /**
     * Vérifier si un concert existe par ID
     */
    public boolean existsById(Long id) {
        return concertRepository.existsById(id);
    }
}
