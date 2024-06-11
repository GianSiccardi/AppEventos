package com.giansiccardi.microservice.msvc_ubicacion.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.giansiccardi.microservice.msvc_ubicacion.entity.pojo.Evento;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ubicacion {

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long id;

@NotBlank
private String nombreSalon;
@NotBlank
private String ciudad;

@NotNull
private Integer capacidad;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name="ubicacion_id")
    private List<UbicacionEvento> ubicacionEventoList;

    @Transient
    private List<Evento> eventos;

    public void addEvento(UbicacionEvento ubicacionEvento){
        ubicacionEventoList.add(ubicacionEvento);
    }

    public void removeEvento(UbicacionEvento ubicacionEvento){
        ubicacionEventoList.remove(ubicacionEvento);
    }
}
