package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.adapter;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Venue;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out.VenueRepositoryPort;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.entity.VenueJpaEntity;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.repository.VenueJpaRepository;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.mapper.VenuePersistenceMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class VenueRepositoryAdapter implements VenueRepositoryPort {

    private final VenueJpaRepository venueJpaRepository;
    private final VenuePersistenceMapper venueMapper;

    public VenueRepositoryAdapter(VenueJpaRepository venueJpaRepository, VenuePersistenceMapper venueMapper) {
        this.venueJpaRepository = venueJpaRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    public Venue save(Venue venue) {
        // Distinguir entre creación y actualización
        if (venue.getId() == null) {
            // Creación
            VenueJpaEntity venueJpaEntity = venueMapper.toJpaEntity(venue);
            VenueJpaEntity savedEntity = venueJpaRepository.save(venueJpaEntity);
            return venueMapper.toDomain(savedEntity);
        } else {
            // Actualización
            VenueJpaEntity existingEntity = venueJpaRepository.findById(venue.getId())
                    .orElseThrow(() -> new RuntimeException("Venue no encontrado para actualizar"));

            // Actualizar solo los campos necesarios, dejando la colección intacta
            existingEntity.setName(venue.getName());
            existingEntity.setCity(venue.getCity());
            existingEntity.setAddress(venue.getAddress());
            existingEntity.setCapacity(venue.getCapacity());

            VenueJpaEntity updatedEntity = venueJpaRepository.save(existingEntity);
            return venueMapper.toDomain(updatedEntity);
        }
    }

    @Override
    public Optional<Venue> findById(Integer id) {
        return venueJpaRepository.findById(id).map(venueMapper::toDomain);
    }

    @Override
    public void deleteById(Integer id) {
        venueJpaRepository.deleteById(id);
    }

    @Override
    public Page<Venue> findAll(Pageable pageable) {
        return venueJpaRepository.findAll(pageable).map(venueMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return venueJpaRepository.existsByName(name);
    }
}
