package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.application.service;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.in.IVenueService;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Venue;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.VenueRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.exception.DuplicateResourceException;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.exception.ResourceNotFoundException;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.VenueRequest;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.response.VenueResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VenueService implements IVenueService {

    private final VenueRepositoryPort venueRepositoryPort;

    public VenueService(VenueRepositoryPort venueRepositoryPort) {
        this.venueRepositoryPort = venueRepositoryPort;
    }

    @Override
    @Transactional
    public VenueResponse create(VenueRequest request) {
        if (venueRepositoryPort.existsByName(request.getName())) {
            throw new DuplicateResourceException("Ya existe un venue con el nombre: " + request.getName());
        }
        Venue venue = toDomain(request);
        Venue savedVenue = venueRepositoryPort.save(venue);
        return toResponse(savedVenue);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<VenueResponse> findAll(Pageable pageable) {
        return venueRepositoryPort.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public VenueResponse findById(Integer id) {
        return venueRepositoryPort.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con el id: " + id));
    }

    @Override
    @Transactional
    public VenueResponse update(Integer id, VenueRequest request) {
        Venue existingVenue = venueRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue no encontrado con el id: " + id));

        if (!existingVenue.getName().equals(request.getName()) && venueRepositoryPort.existsByName(request.getName())) {
            throw new DuplicateResourceException("Ya existe un venue con el nombre: " + request.getName());
        }

        updateDomainFromRequest(existingVenue, request);
        Venue updatedVenue = venueRepositoryPort.save(existingVenue);
        return toResponse(updatedVenue);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        if (!venueRepositoryPort.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Venue no encontrado con el id: " + id);
        }
        venueRepositoryPort.deleteById(id);
    }

    // --- MAPPERS PRIVADOS DE LA CAPA DE APLICACIÓN ---

    private VenueResponse toResponse(Venue domain) {
        VenueResponse response = new VenueResponse();
        response.setId(domain.getId());
        response.setName(domain.getName());
        response.setCity(domain.getCity());
        response.setAddress(domain.getAddress());
        response.setCapacity(domain.getCapacity());
        return response;
    }

    private Venue toDomain(VenueRequest request) {
        Venue domain = new Venue();
        domain.setName(request.getName());
        domain.setCity(request.getCity());
        domain.setAddress(request.getAddress());
        domain.setCapacity(request.getCapacity());
        return domain;
    }

    private void updateDomainFromRequest(Venue domain, VenueRequest request) {
        domain.setName(request.getName());
        domain.setCity(request.getCity());
        domain.setAddress(request.getAddress());
        domain.setCapacity(request.getCapacity());
    }
}
