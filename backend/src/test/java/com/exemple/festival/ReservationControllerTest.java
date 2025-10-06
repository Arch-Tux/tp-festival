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
import java.time.LocalDateTime;
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

import com.exemple.festival.Artist.domain.entities.Artist;
import com.exemple.festival.Concert.domain.entities.Concert;
import com.exemple.festival.Reservation.application.usecase.ReservationService;
import com.exemple.festival.Reservation.domain.entities.Reservation;
import com.exemple.festival.Reservation.infrastructure.controllers.ReservationController;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ReservationController.class)
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;

    @Autowired
    private ObjectMapper objectMapper;

    private Reservation testReservation;
    private Concert testConcert;
    private Artist testArtist;
    private List<Reservation> testReservations;

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

        testReservation = new Reservation();
        testReservation.setId(1L);
        testReservation.setConcert(testConcert);
        testReservation.setEmail("test@example.com");
        testReservation.setQuantity(2);
        testReservation.setReservedAt(LocalDateTime.now());

        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        reservation2.setConcert(testConcert);
        reservation2.setEmail("test2@example.com");
        reservation2.setQuantity(1);
        reservation2.setReservedAt(LocalDateTime.now());

        testReservations = Arrays.asList(testReservation, reservation2);
    }

    // ========== READ OPERATIONS TESTS ==========

    @Test
    void findAll_ShouldReturnAllReservations() throws Exception {
        when(reservationService.findAll()).thenReturn(testReservations);

        mockMvc.perform(get("/api/reservations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].email").value("test2@example.com"));

        verify(reservationService, times(1)).findAll();
    }

    @Test
    void findById_WhenReservationExists_ShouldReturnReservation() throws Exception {
        when(reservationService.findById(1L)).thenReturn(Optional.of(testReservation));

        mockMvc.perform(get("/api/reservations/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.quantity").value(2))
                .andExpect(jsonPath("$.concert.id").value(1));

        verify(reservationService, times(1)).findById(1L);
    }

    @Test
    void findById_WhenReservationNotExists_ShouldReturnNotFound() throws Exception {
        when(reservationService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/999"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).findById(999L);
    }

    @Test
    void count_ShouldReturnReservationCount() throws Exception {
        when(reservationService.count()).thenReturn(5L);

        mockMvc.perform(get("/api/reservations/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(reservationService, times(1)).count();
    }

    @Test
    void findByConcertId_ShouldReturnConcertReservations() throws Exception {
        List<Reservation> concertReservations = Arrays.asList(testReservation);
        when(reservationService.findByConcertId(1L)).thenReturn(concertReservations);

        mockMvc.perform(get("/api/reservations/concert/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].concert.id").value(1));

        verify(reservationService, times(1)).findByConcertId(1L);
    }

    @Test
    void findByEmail_ShouldReturnEmailReservations() throws Exception {
        List<Reservation> emailReservations = Arrays.asList(testReservation);
        when(reservationService.findByEmail("test@example.com")).thenReturn(emailReservations);

        mockMvc.perform(get("/api/reservations/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));

        verify(reservationService, times(1)).findByEmail("test@example.com");
    }

    @Test
    void findByConcertAndEmail_WhenExists_ShouldReturnReservation() throws Exception {
        when(reservationService.findByConcertAndEmail(1L, "test@example.com"))
                .thenReturn(Optional.of(testReservation));

        mockMvc.perform(get("/api/reservations/concert/1/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.concert.id").value(1));

        verify(reservationService, times(1)).findByConcertAndEmail(1L, "test@example.com");
    }

    @Test
    void findByConcertAndEmail_WhenNotExists_ShouldReturnNotFound() throws Exception {
        when(reservationService.findByConcertAndEmail(1L, "notfound@example.com"))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/reservations/concert/1/email/notfound@example.com"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).findByConcertAndEmail(1L, "notfound@example.com");
    }

    @Test
    void countByConcertId_ShouldReturnConcertReservationCount() throws Exception {
        when(reservationService.countByConcertId(1L)).thenReturn(3L);

        mockMvc.perform(get("/api/reservations/concert/1/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("3"));

        verify(reservationService, times(1)).countByConcertId(1L);
    }

    // ========== CREATE OPERATION TESTS ==========

    @Test
    void create_WithValidReservation_ShouldReturnCreatedReservation() throws Exception {
        Reservation newReservation = new Reservation();
        newReservation.setConcert(testConcert);
        newReservation.setEmail("new@example.com");
        newReservation.setQuantity(3);

        Reservation savedReservation = new Reservation();
        savedReservation.setId(1L);
        savedReservation.setConcert(testConcert);
        savedReservation.setEmail("new@example.com");
        savedReservation.setQuantity(3);
        savedReservation.setReservedAt(LocalDateTime.now());

        when(reservationService.save(any(Reservation.class))).thenReturn(savedReservation);

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newReservation)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("new@example.com"))
                .andExpect(jsonPath("$.quantity").value(3));

        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void create_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        Reservation invalidReservation = new Reservation();
        invalidReservation.setConcert(testConcert);
        invalidReservation.setEmail("invalid-email");
        invalidReservation.setQuantity(1);

        when(reservationService.save(any(Reservation.class)))
                .thenThrow(new IllegalArgumentException("Format d'email invalide"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReservation)))
                .andExpect(status().isBadRequest());

        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void create_WithNegativeQuantity_ShouldReturnBadRequest() throws Exception {
        Reservation invalidReservation = new Reservation();
        invalidReservation.setConcert(testConcert);
        invalidReservation.setEmail("test@example.com");
        invalidReservation.setQuantity(-1);

        when(reservationService.save(any(Reservation.class)))
                .thenThrow(new IllegalArgumentException("La quantité doit être positive"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReservation)))
                .andExpect(status().isBadRequest());

        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void create_WithNonExistentConcert_ShouldReturnBadRequest() throws Exception {
        Concert nonExistentConcert = new Concert();
        nonExistentConcert.setId(999L);

        Reservation reservation = new Reservation();
        reservation.setConcert(nonExistentConcert);
        reservation.setEmail("test@example.com");
        reservation.setQuantity(1);

        when(reservationService.save(any(Reservation.class)))
                .thenThrow(new IllegalArgumentException("Le concert spécifié n'existe pas"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isBadRequest());

        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    // ========== UPDATE OPERATION TESTS ==========

    @Test
    void update_WhenReservationExists_ShouldReturnUpdatedReservation() throws Exception {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(1L);
        updatedReservation.setConcert(testConcert);
        updatedReservation.setEmail("updated@example.com");
        updatedReservation.setQuantity(5);

        when(reservationService.existsById(1L)).thenReturn(true);
        when(reservationService.save(any(Reservation.class))).thenReturn(updatedReservation);

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedReservation)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("updated@example.com"))
                .andExpect(jsonPath("$.quantity").value(5));

        verify(reservationService, times(1)).existsById(1L);
        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    @Test
    void update_WhenReservationNotExists_ShouldReturnNotFound() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setConcert(testConcert);
        reservation.setEmail("test@example.com");
        reservation.setQuantity(1);

        when(reservationService.existsById(999L)).thenReturn(false);

        mockMvc.perform(put("/api/reservations/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservation)))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).existsById(999L);
        verify(reservationService, never()).save(any(Reservation.class));
    }

    @Test
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Reservation invalidReservation = new Reservation();
        invalidReservation.setEmail("invalid-email");
        invalidReservation.setQuantity(1);

        when(reservationService.existsById(1L)).thenReturn(true);
        when(reservationService.save(any(Reservation.class)))
                .thenThrow(new IllegalArgumentException("Format d'email invalide"));

        mockMvc.perform(put("/api/reservations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidReservation)))
                .andExpect(status().isBadRequest());

        verify(reservationService, times(1)).existsById(1L);
        verify(reservationService, times(1)).save(any(Reservation.class));
    }

    // ========== DELETE OPERATIONS TESTS ==========

    @Test
    void deleteById_WhenReservationExists_ShouldReturnNoContent() throws Exception {
        when(reservationService.existsById(1L)).thenReturn(true);
        doNothing().when(reservationService).deleteById(1L);

        mockMvc.perform(delete("/api/reservations/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).existsById(1L);
        verify(reservationService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenReservationNotExists_ShouldReturnNotFound() throws Exception {
        when(reservationService.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/reservations/999"))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).existsById(999L);
        verify(reservationService, never()).deleteById(anyLong());
    }

    @Test
    void deleteByConcertId_ShouldReturnNoContent() throws Exception {
        doNothing().when(reservationService).deleteByConcertId(1L);

        mockMvc.perform(delete("/api/reservations/concert/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteByConcertId(1L);
    }

    @Test
    void deleteAll_ShouldReturnNoContent() throws Exception {
        doNothing().when(reservationService).deleteAllAndResetIds();

        mockMvc.perform(delete("/api/reservations/all"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteAllAndResetIds();
    }
}
