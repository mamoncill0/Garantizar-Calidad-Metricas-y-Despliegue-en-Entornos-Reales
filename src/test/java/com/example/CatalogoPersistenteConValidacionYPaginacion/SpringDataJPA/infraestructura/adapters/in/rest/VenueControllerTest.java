package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.in.rest;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.in.IVenueService;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.VenueRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.VenueResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Indica que es una prueba enfocada en la capa web, solo para VenueController.
@WebMvcTest(VenueController.class)
class VenueControllerTest {

    @Autowired
    private MockMvc mockMvc; // Permite simular peticiones HTTP

    // Crea un mock del servicio para aislar el controlador.
    // El controlador pensará que está hablando con el servicio real, pero nosotros controlamos la respuesta.
    @MockBean
    private IVenueService venueService;

    @Autowired
    private ObjectMapper objectMapper; // Utilidad para convertir objetos Java a JSON y viceversa.

    private VenueRequest venueRequest;
    private VenueResponse venueResponse;

    @BeforeEach
    void setUp() {
        // Datos de ejemplo para la petición
        venueRequest = new VenueRequest();
        venueRequest.setName("Teatro Metropolitano");
        venueRequest.setCity("Medellín");
        venueRequest.setAddress("Calle 41 #57-30");
        venueRequest.setCapacity(1634);

        // Datos de ejemplo para la respuesta que simulará el servicio
        venueResponse = new VenueResponse();
        venueResponse.setId(1);
        venueResponse.setName("Teatro Metropolitano");
        venueResponse.setCity("Medellín");
        venueResponse.setAddress("Calle 41 #57-30");
        venueResponse.setCapacity(1634);
    }

    @Test
    void givenVenueRequest_whenCreateVenue_thenReturnSavedVenue() throws Exception {
        // Given: Configuramos el comportamiento del mock.
        // Cuando el mét0do 'create' del servicio sea llamado con cualquier VenueRequest...
        given(venueService.create(any(VenueRequest.class)))
                // ...entonces debe devolver nuestro objeto de respuesta de ejemplo.
                .willReturn(venueResponse);

        // When & Then: Ejecutamos la petición POST y verificamos la respuesta.
        mockMvc.perform(post("/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(venueRequest)))
                .andExpect(status().isOk())
                // Verificamos campo por campo que la respuesta JSON es la correcta.
                // Usamos .value() que es más flexible con los tipos.
                .andExpect(jsonPath("$.name").value(venueResponse.getName()))
                .andExpect(jsonPath("$.city").value(venueResponse.getCity()))
                .andExpect(jsonPath("$.capacity").value(venueResponse.getCapacity()));
    }
}
