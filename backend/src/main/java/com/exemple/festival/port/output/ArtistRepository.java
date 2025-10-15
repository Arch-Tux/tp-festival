package com.exemple.festival.port.output;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.exemple.festival.domain.model.artist.Artist;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, Long> {

    @Modifying
    @Transactional
    @Query(value = "ALTER SEQUENCE artists_id_seq RESTART WITH 1", nativeQuery = true)
    void resetIdSequence();
    
    Optional<Artist> findByName(String name);
    
    List<Artist> findByNameContainingIgnoreCase(String keyword);
    
    boolean existsByName(String name);
}
