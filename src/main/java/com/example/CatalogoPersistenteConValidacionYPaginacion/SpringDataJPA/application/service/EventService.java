package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.application.service;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.in.IEventService;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Event;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Venue;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.EventRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.VenueRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.exception.ResourceNotFoundException;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.EventRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.EventResponse;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.mapper.EventMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EventService implements IEventService {

    private final EventRepositoryPort eventRepositoryPort;
    private final VenueRepositoryPort venueRepositoryPort;
    private final EventMapper mapper;


    public EventService(EventRepositoryPort eventRepositoryPort, VenueRepositoryPort venueRepositoryPort, EventMapper mapper) {
        this.eventRepositoryPort = eventRepositoryPort;
        this.venueRepositoryPort = venueRepositoryPort;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public EventResponse create(EventRequest request) {
        Venue venue = venueRepositoryPort.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con el id: " + request.getVenueId()));

        Event event = mapper.toDomain(request);
        Event savedEvent = eventRepositoryPort.save(event);
        return toResponse(savedEvent);
    }

    @Override
    @Transactional
    public EventResponse update(Integer id, EventRequest request) {
        Event existingEvent = eventRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con el id: " + id));

        Venue venue = venueRepositoryPort.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con el id: " + request.getVenueId()));

        updateDomainFromRequest(existingEvent, request, venue);
        Event updatedEvent = eventRepositoryPort.save(existingEvent);
        return toResponse(updatedEvent);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!eventRepositoryPort.existsById(id)) {
            throw new ResourceNotFoundException("Evento no encontrado con el id: " + id);
        }
        eventRepositoryPort.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponse getById(Integer id) {
        return eventRepositoryPort.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Evento no encontrado con el id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponse> getAll(String city, String category, LocalDateTime startDate, Pageable pageable) {
        return eventRepositoryPort.findAll(pageable).map(this::toResponse);
    }

    // --- MAPPERS ---

    private EventResponse toResponse(Event domain) {
        EventResponse response = new EventResponse();
        response.setId(domain.getId());
        response.setNameEvent(domain.getNameEvent());
        response.setStartTime(domain.getStartTime());
        response.setEndTime(domain.getEndTime());
        response.setDescription(domain.getDescription());
        response.setCapacity(domain.getCapacity());
        if (domain.getVenue() != null) {
            response.setVenueId(domain.getVenue().getId());
        }
        return response;
    }

//    private Event toDomain(EventRequest request, Venue venue) {
//        Event domain = new Event();
//        domain.setNameEvent(request.getNameEvent());
//        domain.setStartTime(request.getStartTime());
//        domain.setEndTime(request.getEndTime());
//        domain.setDescription(request.getDescription());
//        domain.setCapacity(request.getCapacity());
//        domain.setVenue(venue);
//        return domain;
////    private Event toDomain(EventRequest request, Venue venue) {
//        Event domain = new Event();
//        domain.setNameEvent(request.getNameEvent());
//        domain.setStartTime(request.getStartTime());
//        domain.setEndTime(request.getEndTime());
//        domain.setDescription(request.getDescription());
//        domain.setCapacity(request.getCapacity());
//        domain.setVenue(venue);
//        return domain;
//    }

    private void updateDomainFromRequest(Event domain, EventRequest request, Venue venue) {
        domain.setNameEvent(request.getNameEvent());
        domain.setStartTime(request.getStartTime());
        domain.setEndTime(request.getEndTime());
        domain.setDescription(request.getDescription());
        domain.setCapacity(request.getCapacity());
        domain.setVenue(venue);
    }
}
