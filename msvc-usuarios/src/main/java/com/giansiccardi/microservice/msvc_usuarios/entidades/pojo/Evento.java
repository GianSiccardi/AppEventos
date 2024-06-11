package com.giansiccardi.microservice.msvc_usuarios.entidades.pojo;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Evento {


    private Long id;


    private String nombreEvento;


    private Integer cantidadEntradas;

    private Double precioEntradas;
}
