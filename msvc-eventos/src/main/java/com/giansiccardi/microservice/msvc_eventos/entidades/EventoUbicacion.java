package com.giansiccardi.microservice.msvc_eventos.entidades;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Objects;

@Entity
@Data
@Table(name="ubicacion_evento")
public class EventoUbicacion {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

private Long ubicacionId;


    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof EventoUbicacion)
            return false;
        EventoUbicacion ubi = (EventoUbicacion) o;
        return this.ubicacionId !=null && this.ubicacionId.equals(ubi.ubicacionId);
    }


}
