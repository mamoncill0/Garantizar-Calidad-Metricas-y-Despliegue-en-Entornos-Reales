package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.mapper;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Event;
import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request.EventRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "venueId", target = "venue.id")
    Event toDomain(EventRequest request);
}
