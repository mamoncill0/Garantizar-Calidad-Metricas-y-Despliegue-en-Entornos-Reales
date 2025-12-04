package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.application.service;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Venue;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.VenueRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.exception.DuplicateResourceException;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.VenueRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.VenueResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VenueServiceTest {

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    @InjectMocks
    private VenueService venueService;

    private VenueRequest venueRequest;
    private Venue venue;

    @BeforeEach
    void setUp() {
        // Objeto de entrada para las pruebas
        venueRequest = new VenueRequest();
        venueRequest.setName("Estadio Atanasio Girardot");
        venueRequest.setCity("Medellín");
        venueRequest.setAddress("Cra. 74 #48-21");
        venueRequest.setCapacity(45000);

        // Objeto de dominio que esperamos que el repositorio guarde y devuelva
        venue = new Venue();
        venue.setId(1);
        venue.setName("Estadio Atanasio Girardot");
        venue.setCity("Medellín");
        venue.setAddress("Cra. 74 #48-21");
        venue.setCapacity(45000);
    }

    @Test
    void givenNewVenueRequest_whenCreateVenue_thenVenueIsSavedAndReturned() {
        // Given: Configuramos el comportamiento de nuestros mocks
        // 1. Cuando se verifique si el nombre existe, devolvemos false (no existe)
        given(venueRepositoryPort.existsByName(venueRequest.getName())).willReturn(false);
        // 2. Cuando se guarde cualquier objeto Venue, devolvemos nuestro objeto 'venue' de ejemplo
        given(venueRepositoryPort.save(any(Venue.class))).willReturn(venue);

        // When: Ejecutamos el mét0do que queremos probar
        VenueResponse savedVenueResponse = venueService.create(venueRequest);

        // Then: Verificamos que el resultado es el esperado
        assertThat(savedVenueResponse).isNotNull();
        assertThat(savedVenueResponse.getName()).isEqualTo("Estadio Atanasio Girardot");
        assertThat(savedVenueResponse.getId()).isEqualTo(1);
    }

    @Test
    void givenExistingVenueName_whenCreateVenue_thenThrowsDuplicateResourceException() {
        // Given: Configuramos el mock para que indique que el nombre ya existe
        given(venueRepositoryPort.existsByName(venueRequest.getName())).willReturn(true);

        // When & Then: Verificamos que al llamar a create, se lanza la excepción esperada
        assertThrows(DuplicateResourceException.class, () -> {
            venueService.create(venueRequest);
        });

        // Adicionalmente, verificamos que el método 'save' del repositorio NUNCA fue llamado
        verify(venueRepositoryPort, never()).save(any(Venue.class));
    }
}
