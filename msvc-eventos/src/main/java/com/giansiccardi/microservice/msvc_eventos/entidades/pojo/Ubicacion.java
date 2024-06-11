package com.giansiccardi.microservice.msvc_eventos.entidades.pojo;

import com.giansiccardi.microservice.msvc_eventos.entidades.Evento;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class Ubicacion {

    private Long id;
    private String nombreSalon;
    private String ciudad;
    private Integer capacidad;


}
