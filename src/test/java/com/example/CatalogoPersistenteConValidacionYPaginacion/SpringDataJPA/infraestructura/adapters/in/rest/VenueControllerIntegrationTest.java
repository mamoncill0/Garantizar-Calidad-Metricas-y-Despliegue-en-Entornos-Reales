package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.in.rest;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.VenueRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.VenueResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class VenueControllerIntegrationTest {

    @Container
    static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    @DynamicPropertySource
    static void setDatasourceProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
        // Desactivamos la seguridad para esta prueba de integración para simplificar
        registry.add("spring.security.enabled", () -> "false");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void givenVenueRequest_whenCreateVenue_thenVenueIsSavedInDb() {
        // Given
        VenueRequest venueRequest = new VenueRequest();
        venueRequest.setName("Pabellón Amarillo");
        venueRequest.setCity("Buenos Aires");
        venueRequest.setAddress("Av. Sarmiento 2704");
        venueRequest.setCapacity(10000);

        // When
        ResponseEntity<VenueResponse> response = restTemplate.postForEntity("/venues", venueRequest, VenueResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Pabellón Amarillo");

        // Opcional: Verificar directamente en la base de datos (requeriría un JpaRepository en la prueba)
        // O simplemente, podemos usar el endpoint GET para verificar que se guardó
        ResponseEntity<VenueResponse> getResponse = restTemplate.getForEntity("/venues/" + response.getBody().getId(), VenueResponse.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().getName()).isEqualTo("Pabellón Amarillo");
    }
}
