package com.exemple.festival.config;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.exemple.festival.business.entities.Artist;
import com.exemple.festival.business.entities.Concert;
import com.exemple.festival.business.services.ArtistService;
import com.exemple.festival.business.services.ConcertService;

import net.datafaker.Faker;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ArtistService artistService;

    @Autowired
    private ConcertService concertService;

    @Override
    public void run(String... args) throws Exception {
        seedDatabase();
    }

    private void seedDatabase() {
        // Ne seed que si la base est vide
        if (artistService.count() == 0) {
            System.out.println("🌱 Démarrage du seeding de la base de données...");
            
            // Créer 50 artistes
            List<Artist> artists = createArtists();
            System.out.println("✅ " + artists.size() + " artistes créés");
            
            // Créer 2 concerts avec les premiers artistes
            createConcerts(artists);
            System.out.println("✅ 2 concerts créés");
            
            System.out.println("🎉 Seeding terminé avec succès !");
        } else {
            System.out.println("📊 Base de données déjà peuplée - seeding ignoré");
        }
    }

    private List<Artist> createArtists() {
        Faker faker = new Faker();
        Set<String> usedNames = new HashSet<>();
        List<Artist> artists = new ArrayList<>();
        
        while (artists.size() < 50) {
            String artistName;
            
            int nameType = faker.random().nextInt(4);
            switch (nameType) {
                case 0:
                    artistName = "The " + faker.color().name() + " " + faker.animal().name() + "s";
                    break;
                case 1:
                    artistName = faker.name().firstName() + " " + faker.name().lastName();
                    break;
                default:
                    artistName = faker.name().firstName() + " " + faker.name().lastName();
                    break;
            }
            
            artistName = artistName.replaceAll("[^a-zA-Z0-9\\s&]", "").trim();
            
            if (!usedNames.contains(artistName) && artistName.length() > 3) {
                usedNames.add(artistName);
                
                Artist artist = new Artist();
                artist.setName(artistName);
                artists.add(artistService.save(artist));
            }
        }
        
        return artists;
    }

    private void createConcerts(List<Artist> artists) {
        Random random = new Random();
        
        // Concert 1 - Artiste aléatoire
        Concert concert1 = new Concert();
        concert1.setArtist(artists.get(random.nextInt(artists.size()))); // Artiste aléatoire
        concert1.setStartsAt(generateRandomDate(random)); // Date aléatoire
        concert1.setCapacity(random.nextInt(8000) + 2000); // Capacité entre 2000 et 10000
        concertService.save(concert1);

        // Concert 2 - Artiste aléatoire (différent du premier)
        Concert concert2 = new Concert();
        Artist artist2;
        do {
            artist2 = artists.get(random.nextInt(artists.size()));
        } while (artist2.getId().equals(concert1.getArtist().getId())); // S'assurer que c'est différent
        
        concert2.setArtist(artist2);
        concert2.setStartsAt(generateRandomDate(random)); // Date aléatoire
        concert2.setCapacity(random.nextInt(8000) + 2000); // Capacité entre 2000 et 10000
        concertService.save(concert2);
    }
    
    /**
     * Génère une date aléatoire entre maintenant et fin 2025
     */
    private Date generateRandomDate(Random random) {
        LocalDate startDate = LocalDate.now(); // Date actuelle
        LocalDate endDate = LocalDate.of(2025, 12, 31); // Fin 2025
        
        long daysBetween = startDate.toEpochDay() - endDate.toEpochDay();
        long randomDays = Math.abs(random.nextLong() % Math.abs(daysBetween));
        
        LocalDate randomDate = startDate.plusDays(randomDays);
        return Date.valueOf(randomDate);
    }
}