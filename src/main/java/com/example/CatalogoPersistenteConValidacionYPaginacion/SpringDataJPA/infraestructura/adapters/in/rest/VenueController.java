package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.in.rest;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.in.IVenueService;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.VenueRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.VenueResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/venues")
public class VenueController {

    private static final Logger log = LoggerFactory.getLogger(VenueController.class);

    @Autowired
    private IVenueService venueService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public VenueResponse create(@Valid @RequestBody VenueRequest dto) {
        log.info("Creando un nuevo venue con nombre: {}", dto.getName());
        return venueService.create(dto);
    }

    @GetMapping
    public Page<VenueResponse> getAll(Pageable pageable) {
        log.info("Obteniendo todos los venues");
        return venueService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public VenueResponse getById(@PathVariable Integer id) {
        log.info("Obteniendo el venue con id: {}", id);
        return venueService.findById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public VenueResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody VenueRequest dto) {
        log.info("Actualizando el venue con id: {}", id);
        return venueService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Integer id) {
        log.info("Eliminando el venue con id: {}", id);
        venueService.delete(id);
    }
}
