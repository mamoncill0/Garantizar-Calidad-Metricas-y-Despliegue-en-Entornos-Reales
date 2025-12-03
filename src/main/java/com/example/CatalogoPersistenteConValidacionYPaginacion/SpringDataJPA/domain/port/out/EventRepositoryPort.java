package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.port.out;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

// Puerto de salida para la persistencia de Events.
public interface EventRepositoryPort {

    Event save(Event event);

    Optional<Event> findById(Integer id);

    void deleteById(Integer id);

    Page<Event> findAll(Pageable pageable);

    boolean existsById(Integer id);
}