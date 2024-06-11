package com.giansiccardi.microservice.msvc_ubicacion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="evento_ubicacion")
public class UbicacionEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long eventoId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof UbicacionEvento)
            return false;
        UbicacionEvento ubi = (UbicacionEvento) o;
        return this.eventoId !=null && this.eventoId.equals(ubi.eventoId);
    }

}
