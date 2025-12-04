package com.example.CatalogoPersistenteConValidacionYPaginacion.SpringDataJPA.domain.model;

import java.util.List;

// Este es un objeto de dominio puro (POJO).
// No tiene ninguna anotación de frameworks. Representa el concepto de "Venue" en nuestro negocio.
public class Venue {

    private Integer id;
    private String name;
    private String city;
    private String address;
    private int capacity; // Propiedad de tipo int
    private List<Event> events; // Se relaciona con el objeto de dominio puro Event

    // Constructor, Getters y Setters...

    public Venue() {
    }

    public Venue(Integer id, String name, String city, String address, int capacity) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.capacity = capacity;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() { // <-- CORREGIDO: Ahora devuelve int
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
