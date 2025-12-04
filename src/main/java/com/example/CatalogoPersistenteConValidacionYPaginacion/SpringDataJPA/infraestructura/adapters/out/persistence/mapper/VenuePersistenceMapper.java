package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.mapper;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Venue;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.adapters.out.persistence.entity.VenueJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class VenuePersistenceMapper {

    public Venue toDomain(VenueJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        Venue venue = new Venue();
        venue.setId(jpaEntity.getId());
        venue.setName(jpaEntity.getName());
        venue.setCity(jpaEntity.getCity());
        venue.setAddress(jpaEntity.getAddress());
        venue.setCapacity(jpaEntity.getCapacity()); // <-- CORREGIDO: Se pasa el int directamente
        return venue;
    }

    public VenueJpaEntity toJpaEntity(Venue venue) {
        if (venue == null) {
            return null;
        }
        VenueJpaEntity jpaEntity = new VenueJpaEntity();
        jpaEntity.setId(venue.getId());
        jpaEntity.setName(venue.getName());
        jpaEntity.setCity(venue.getCity());
        jpaEntity.setAddress(venue.getAddress());
        jpaEntity.setCapacity(venue.getCapacity());
        return jpaEntity;
    }
}
