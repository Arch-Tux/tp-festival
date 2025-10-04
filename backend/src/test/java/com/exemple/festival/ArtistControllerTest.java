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

import com.exemple.festival.business.entities.Artist;
import com.exemple.festival.business.services.ArtistService;
import com.exemple.festival.presentation.ArtistController;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ArtistController.class)
public class ArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArtistService artistService;

    @Autowired
    private ObjectMapper objectMapper;

    private Artist testArtist;
    private List<Artist> testArtists;

    @BeforeEach
    void setUp() {
        testArtist = new Artist();
        testArtist.setId(1L);
        testArtist.setName("Test Artist");

        Artist artist2 = new Artist();
        artist2.setId(2L);
        artist2.setName("Another Artist");

        testArtists = Arrays.asList(testArtist, artist2);
    }

    // ========== READ OPERATIONS TESTS ==========

    @Test
    void findAll_ShouldReturnAllArtists() throws Exception {
        when(artistService.findAll()).thenReturn(testArtists);

        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Artist"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Another Artist"));

        verify(artistService, times(1)).findAll();
    }

    @Test
    void findById_WhenArtistExists_ShouldReturnArtist() throws Exception {
        when(artistService.findById(1L)).thenReturn(Optional.of(testArtist));

        mockMvc.perform(get("/api/artists/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Artist"));

        verify(artistService, times(1)).findById(1L);
    }

    @Test
    void findById_WhenArtistNotExists_ShouldReturnNotFound() throws Exception {
        when(artistService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/artists/999"))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).findById(999L);
    }

    @Test
    void count_ShouldReturnArtistCount() throws Exception {
        when(artistService.count()).thenReturn(5L);

        mockMvc.perform(get("/api/artists/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(artistService, times(1)).count();
    }

    @Test
    void searchByName_ShouldReturnMatchingArtists() throws Exception {
        List<Artist> searchResults = Arrays.asList(testArtist);
        when(artistService.searchByName("Test")).thenReturn(searchResults);

        mockMvc.perform(get("/api/artists/search")
                        .param("keyword", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Artist"));

        verify(artistService, times(1)).searchByName("Test");
    }

    // ========== CREATE OPERATION TESTS ==========

    @Test
    void create_WithValidArtist_ShouldReturnCreatedArtist() throws Exception {
        Artist newArtist = new Artist();
        newArtist.setName("New Artist");

        Artist savedArtist = new Artist();
        savedArtist.setId(1L);
        savedArtist.setName("New Artist");

        when(artistService.save(any(Artist.class))).thenReturn(savedArtist);

        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArtist)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Artist"));

        verify(artistService, times(1)).save(any(Artist.class));
    }

    @Test
    void create_WithInvalidArtist_ShouldReturnBadRequest() throws Exception {
        Artist invalidArtist = new Artist();
        invalidArtist.setName("Duplicate Artist");

        when(artistService.save(any(Artist.class)))
                .thenThrow(new IllegalArgumentException("Un artiste avec ce nom existe déjà"));

        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidArtist)))
                .andExpect(status().isBadRequest());

        verify(artistService, times(1)).save(any(Artist.class));
    }

    // ========== UPDATE OPERATION TESTS ==========

    @Test
    void update_WhenArtistExists_ShouldReturnUpdatedArtist() throws Exception {
        Artist updatedArtist = new Artist();
        updatedArtist.setId(1L);
        updatedArtist.setName("Updated Artist");

        when(artistService.existsById(1L)).thenReturn(true);
        when(artistService.save(any(Artist.class))).thenReturn(updatedArtist);

        mockMvc.perform(put("/api/artists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedArtist)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Artist"));

        verify(artistService, times(1)).existsById(1L);
        verify(artistService, times(1)).save(any(Artist.class));
    }

    @Test
    void update_WhenArtistNotExists_ShouldReturnNotFound() throws Exception {
        Artist artist = new Artist();
        artist.setName("Non-existent Artist");

        when(artistService.existsById(999L)).thenReturn(false);

        mockMvc.perform(put("/api/artists/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artist)))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).existsById(999L);
        verify(artistService, never()).save(any(Artist.class));
    }

    @Test
    void update_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        Artist invalidArtist = new Artist();
        invalidArtist.setName("");

        when(artistService.existsById(1L)).thenReturn(true);
        when(artistService.save(any(Artist.class)))
                .thenThrow(new IllegalArgumentException("Le nom de l'artiste ne peut pas être vide"));

        mockMvc.perform(put("/api/artists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidArtist)))
                .andExpect(status().isBadRequest());

        verify(artistService, times(1)).existsById(1L);
        verify(artistService, times(1)).save(any(Artist.class));
    }

    // ========== DELETE OPERATIONS TESTS ==========

    @Test
    void deleteById_WhenArtistExists_ShouldReturnNoContent() throws Exception {
        when(artistService.existsById(1L)).thenReturn(true);
        doNothing().when(artistService).deleteById(1L);

        mockMvc.perform(delete("/api/artists/1"))
                .andExpect(status().isNoContent());

        verify(artistService, times(1)).existsById(1L);
        verify(artistService, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_WhenArtistNotExists_ShouldReturnNotFound() throws Exception {
        when(artistService.existsById(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/artists/999"))
                .andExpect(status().isNotFound());

        verify(artistService, times(1)).existsById(999L);
        verify(artistService, never()).deleteById(anyLong());
    }

    @Test
    void deleteAll_ShouldReturnNoContent() throws Exception {
        doNothing().when(artistService).deleteAllAndResetIds();

        mockMvc.perform(delete("/api/artists/all"))
                .andExpect(status().isNoContent());

        verify(artistService, times(1)).deleteAllAndResetIds();
    }
}
