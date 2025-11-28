package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.in.rest;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.in.IEventService;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.EventRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.EventResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/events")
public class EventController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    private IEventService eventService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse create(@Valid @RequestBody EventRequest dto) {
        log.info("Creando un nuevo evento con nombre: {}", dto.getNameEvent());
        return eventService.create(dto);
    }

    @GetMapping
    public Page<EventResponse> getAll(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDateTime startDate,
            Pageable pageable
    ) {
        log.info("Obteniendo todos los eventos con los filtros: city={}, category={}, startDate={}", city, category, startDate);
        return eventService.getAll(city, category, startDate, pageable);
    }

    @GetMapping("/{id}")
    public EventResponse getById(@PathVariable Integer id) {
        log.info("Obteniendo el evento con id: {}", id);
        return eventService.getById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public EventResponse update(
            @PathVariable Integer id,
            @Valid @RequestBody EventRequest dto) {
        log.info("Actualizando el evento con id: {}", id);
        return eventService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable Integer id) {
        log.info("Eliminando el evento con id: {}", id);
        eventService.delete(id);
    }
}
