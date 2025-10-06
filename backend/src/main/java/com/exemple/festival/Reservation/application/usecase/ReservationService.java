package com.exemple.festival.Reservation.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.Concert.infrastructure.repositories.ConcertRepository;
import com.exemple.festival.Reservation.domain.entities.Reservation;
import com.exemple.festival.Reservation.infrastructure.repositories.ReservationRepository;
//TODO Découper en use case
@Service
@Transactional
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private ConcertRepository concertRepository;
    
    // Pattern pour validation email basique
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    // ========== OPÉRATIONS CRUD ==========
    
    /**
     * Créer ou modifier une réservation
     */
    public Reservation save(Reservation reservation) {
        // Validations de base
        if (reservation.getEmail() == null || reservation.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'email ne peut pas être vide");
        }
        
        if (!EMAIL_PATTERN.matcher(reservation.getEmail().trim()).matches()) {
            throw new IllegalArgumentException("Format d'email invalide");
        }
        
        if (reservation.getQuantity() == null || reservation.getQuantity() <= 0) {
            throw new IllegalArgumentException("La quantité doit être positive");
        }
        
        if (reservation.getConcert() == null || reservation.getConcert().getId() == null) {
            throw new IllegalArgumentException("Le concert est obligatoire");
        }
        
        // Vérifier que le concert existe
        if (!concertRepository.existsById(reservation.getConcert().getId())) {
            throw new IllegalArgumentException("Le concert spécifié n'existe pas");
        }
        
        // Lors de la création, définir la date de réservation
        if (reservation.getId() == null) {
            reservation.setReservedAt(LocalDateTime.now());
        }
        
        // Normaliser l'email
        reservation.setEmail(reservation.getEmail().trim().toLowerCase());
        
        return reservationRepository.save(reservation);
    }
    
    /**
     * Trouver une réservation par ID
     */
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id);
    }
    
    /**
     * Trouver toutes les réservations
     */
    public List<Reservation> findAll() {
        return reservationRepository.findAll();
    }
    
    /**
     * Supprimer une réservation par ID
     */
    public void deleteById(Long id) {
        reservationRepository.deleteById(id);
    }
    
    /**
     * Supprimer toutes les réservations et reset des IDs
     */
    public void deleteAllAndResetIds() {
        reservationRepository.deleteAll();
        reservationRepository.resetIdSequence();
    }
    
    // ========== MÉTHODES SUPPLÉMENTAIRES ==========
    
    /**
     * Trouver les réservations d'un concert
     */
    public List<Reservation> findByConcertId(Long concertId) {
        return reservationRepository.findByConcertId(concertId);
    }
    
    /**
     * Trouver les réservations d'un email
     */
    public List<Reservation> findByEmail(String email) {
        return reservationRepository.findByEmail(email.trim().toLowerCase());
    }
    
    /**
     * Trouver une réservation pour un concert et un email
     */
    public Optional<Reservation> findByConcertAndEmail(Long concertId, String email) {
        return reservationRepository.findByConcertIdAndEmail(concertId, email.trim().toLowerCase());
    }
    
    /**
     * Compter les réservations pour un concert
     */
    public Long countByConcertId(Long concertId) {
        return reservationRepository.countByConcertId(concertId);
    }
    
    /**
     * Supprimer toutes les réservations d'un concert
     */
    public void deleteByConcertId(Long concertId) {
        reservationRepository.deleteByConcertId(concertId);
    }
    
    /**
     * Compter le nombre total de réservations
     */
    public long count() {
        return reservationRepository.count();
    }
    
    /**
     * Vérifier si une réservation existe par ID
     */
    public boolean existsById(Long id) {
        return reservationRepository.existsById(id);
    }
}
