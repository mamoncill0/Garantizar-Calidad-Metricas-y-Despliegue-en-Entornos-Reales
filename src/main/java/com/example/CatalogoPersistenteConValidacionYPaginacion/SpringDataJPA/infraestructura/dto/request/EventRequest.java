package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.request;

import com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.infraestructura.dto.validation.DateRange;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
@DateRange
public class EventRequest {

    @NotBlank(message = "Event name cannot be empty")
    @Size(max = 150, message = "Event name must be <= 150 characters")
    private String nameEvent;

    @NotNull(message = "Start time is required")
    @Future(message = "Start time must be in the future")
    private Date startTime;

    @NotNull(message = "End time is required")
    @Future(message = "End time must be in the future")
    private Date endTime;

    private String description;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;


    private Integer venueId;
}
