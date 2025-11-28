package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.adapter;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Event;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.EventRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.entity.EventJpaEntity;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.entity.VenueJpaEntity;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.repository.EventJpaRepository;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.repository.VenueJpaRepository;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.mapper.EventPersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class EventRepositoryAdapter implements EventRepositoryPort {

    private final EventJpaRepository eventJpaRepository;
    private final EventPersistenceMapper eventMapper;
    private final VenueJpaRepository venueJpaRepository;

    public EventRepositoryAdapter(EventJpaRepository eventJpaRepository, EventPersistenceMapper eventMapper, VenueJpaRepository venueJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
        this.eventMapper = eventMapper;
        this.venueJpaRepository = venueJpaRepository;
    }

    @Override
    public Event save(Event event) {
        if (event.getId() == null) {
            // Creación
            EventJpaEntity eventJpaEntity = eventMapper.toJpaEntity(event);
            EventJpaEntity savedEntity = eventJpaRepository.save(eventJpaEntity);
            return eventMapper.toDomain(savedEntity);
        } else {
            // Actualización
            EventJpaEntity existingEntity = eventJpaRepository.findById(event.getId())
                    .orElseThrow(() -> new RuntimeException("Evento no encontrado para actualizar"));

            // Actualizar campos
            existingEntity.setNameEvent(event.getNameEvent());
            existingEntity.setStartTime(event.getStartTime());
            existingEntity.setEndTime(event.getEndTime());
            existingEntity.setDescription(event.getDescription());
            existingEntity.setCapacity(event.getCapacity());

            // Manejar la relación con Venue
            if (event.getVenue() != null && event.getVenue().getId() != null) {
                VenueJpaEntity venueEntity = venueJpaRepository.findById(event.getVenue().getId())
                        .orElseThrow(() -> new RuntimeException("Venue no encontrado para el evento"));
                existingEntity.setVenue(venueEntity);
            } else {
                existingEntity.setVenue(null);
            }

            EventJpaEntity updatedEntity = eventJpaRepository.save(existingEntity);
            return eventMapper.toDomain(updatedEntity);
        }
    }

    @Override
    public Optional<Event> findById(Integer id) {
        return eventJpaRepository.findById(id).map(eventMapper::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        eventJpaRepository.deleteById(id);
    }

    @Override
    public Page<Event> findAll(Pageable pageable) {
        return eventJpaRepository.findAll(pageable).map(eventMapper::toDomain);
    }

    @Override
    public boolean existsById(Integer id) {
        return eventJpaRepository.existsById(id);
    }
}
