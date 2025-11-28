package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
