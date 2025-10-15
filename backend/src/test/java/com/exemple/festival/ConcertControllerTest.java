package com.exemple.festival;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.exemple.festival.Concert.application.usecase.ConcertService;
import com.exemple.festival.adapter.concert.ConcertController;
import com.exemple.festival.domain.model.artist.Artist;
import com.exemple.festival.domain.model.concert.Concert;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ConcertController.class)
public class ConcertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConcertService concertService;

    @Autowired
    private ObjectMapper objectMapper;

    private Concert testConcert;
    private Artist testArtist;
    private List<Concert> testConcerts;

    @BeforeEach
    void setUp() {
        testArtist = new Artist();
        testArtist.setId(1L);
        testArtist.setName("Test Artist");

        testConcert = new Concert();
        testConcert.setId(1L);
        testConcert.setArtist(testArtist);
        testConcert.setStartsAt(new Date(System.currentTimeMillis()));
        testConcert.setCapacity(100);

        Concert concert2 = new Concert();
        concert2.setId(2L);
        concert2.setArtist(testArtist);
        concert2.setStartsAt(new Date(System.currentTimeMillis()));
        concert2.setCapacity(200);

        testConcerts = Arrays.asList(testConcert, concert2);
    }

    // ========== READ OPERATIONS TESTS ==========

    @Test
    void findAll_ShouldReturnAllConcerts() throws Exception {
        when(concertService.findAll()).thenReturn(testConcerts);

        mockMvc.perform(get("/api/concerts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].capacity").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].capacity").value(200));

        verify(concertService, times(1)).findAll();
    }

    @Test
    void findById_WhenConcertExists_ShouldReturnConcert() throws Exception {
        when(concertService.findById(1L)).thenReturn(Optional.of(testConcert));

        mockMvc.perform(get("/api/concerts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.capacity").value(100))
                .andExpect(jsonPath("$.artist.id").value(1))
                .andExpect(jsonPath("$.artist.name").value("Test Artist"));

        verify(concertService, times(1)).findById(1L);
    }

    @Test
    void findById_WhenConcertNotExists_ShouldReturnNotFound() throws Exception {
        when(concertService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/concerts/999"))
                .andExpect(status().isNotFound());

        verify(concertService, times(1)).findById(999L);
    }

    @Test
    void count_ShouldReturnConcertCount() throws Exception {
        when(concertService.count()).thenReturn(3L);

        mockMvc.perform(get("/api/concerts/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(concertService, times(1)).count();
    }

    @Test
    void findByArtistId_ShouldReturnArtistConcerts() throws Exception {
        List<Concert> artistConcerts = Arrays.asList(testConcert);
        when(concertService.findByArtistId(1L)).thenReturn(artistConcerts);

        mockMvc.perform(get("/api/concerts/artist/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].artist.id").value(1));

        verify(concertService, times(1)).findByArtistId(1L);
    }

    // ========== CREATE OPERATION TESTS ==========

    @Test
    void create_WithValidConcert_ShouldReturnCreatedConcert() throws Exception {
        Concert newConcert = new Concert();
        newConcert.setArtist(testArtist);
        newConcert.setStartsAt(new Date(System.currentTimeMillis()));
        newConcert.setCapacity(150);

        Concert savedConcert = new Concert();
        savedConcert.setId(1L);
        savedConcert.setArtist(testArtist);
        savedConcert.setStartsAt(newConcert.getStartsAt());
        savedConcert.setCapacity(150);

        when(concertService.save(any(Concert.class))).thenReturn(savedConcert);

        mockMvc.perform(post("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newConcert)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.capacity").value(150));

        verify(concertService, times(1)).save(any(Concert.class));
    }

    @Test
    void create_WithInvalidConcert_ShouldReturnBadRequest() throws Exception {
        Concert invalidConcert = new Concert();
        invalidConcert.setCapacity(-10); // Capacité négative

        when(concertService.save(any(Concert.class)))
                .thenThrow(new IllegalArgumentException("La capacité doit être positive"));

        mockMvc.perform(post("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidConcert)))
                .andExpect(status().isBadRequest());

        verify(concertService, times(1)).save(any(Concert.class));
    }

    @Test
    void create_WithNonExistentArtist_ShouldReturnBadRequest() throws Exception {
        Artist nonExistentArtist = new Artist();
        nonExistentArtist.setId(999L);

        Concert concert = new Concert();
        concert.setArtist(nonExistentArtist);
        concert.setStartsAt(new Date(System.currentTimeMillis()));
        concert.setCapacity(100);

        when(concertService.save(any(Concert.class)))
                .thenThrow(new IllegalArgumentException("L'artiste spécifié n'existe pas"));

        mockMvc.perform(post("/api/concerts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isBadRequest());

        verify(concertService, times(1)).save(any(Concert.class));
    }

    // ========== UPDATE OPERATION TESTS ==========

    @Test
    void update_WhenConcertExists_ShouldReturnUpdatedConcert() throws Exception {
        Concert updatedConcert = new Concert();
        updatedConcert.setId(1L);
        updatedConcert.setArtist(testArtist);
        updatedConcert.setStartsAt(new Date(System.currentTimeMillis()));
        updatedConcert.setCapacity(250);

        when(concertService.existsById(1L)).thenReturn(true);
        when(concertService.save(any(Concert.class))).thenReturn(updatedConcert);

        mockMvc.perform(put("/api/concerts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedConcert)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.capacity").value(250));

        verify(concertService, times(1)).existsById(1L);
        verify(concertService, times(1)).save(any(Concert.class));
    }

    @Test
    void update_WhenConcertNotExists_ShouldReturnNotFound() throws Exception {
        Concert concert = new Concert();
        concert.setArtist(testArtist);
        concert.setStartsAt(new Date(System.currentTimeMillis()));
        concert.setCapacity(100);

        when(concertService.existsById(999L)).thenReturn(false);

        mockMvc.perform(put("/api/concerts/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(concert)))
                .andExpect(status().isNotFound());

        verify(concertService, times(1)).existsById(999L);
        verify(concertService, never()).save(any(Concert.class));
    }

    @Test
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Concert invalidConcert = new Concert();
        invalidConcert.setCapacity(0); // Capacité invalide

        when(concertService.existsById(1L)).thenReturn(true);
        when(concertService.save(any(Concert.class)))
                .thenThrow(new IllegalArgumentException("La capacité doit être positive"));

        mockMvc.perform(put("/api/concerts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidConcert)))
                .andExpect(status().isBadRequest());

        verify(concertService, times(1)).existsById(1L);
        verify(concertService, times(1)).save(any(Concert.class));
    }

    // ========== DELETE OPERATIONS TESTS ==========

    @Test
    void deleteById_WhenConcertExists_ShouldReturnNoContent() throws Exception {
        when(concertService.existsById(1L)).thenReturn(true);
        doNothing().when(concertService).deleteById(1L);

        mockMvc.perform(delete("/api/concerts/1"))
                .andExpect(status().isNoContent());

        verify(concertService, times(1)).existsById(1L);
        verify(concertService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenConcertNotExists_ShouldReturnNotFound() throws Exception {
        when(concertService.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/concerts/999"))
                .andExpect(status().isNotFound());

        verify(concertService, times(1)).existsById(999L);
        verify(concertService, never()).deleteById(anyLong());
    }

    @Test
    void deleteAll_ShouldReturnNoContent() throws Exception {
        doNothing().when(concertService).deleteAllAndResetIds();

        mockMvc.perform(delete("/api/concerts/all"))
                .andExpect(status().isNoContent());

        verify(concertService, times(1)).deleteAllAndResetIds();
    }
}
