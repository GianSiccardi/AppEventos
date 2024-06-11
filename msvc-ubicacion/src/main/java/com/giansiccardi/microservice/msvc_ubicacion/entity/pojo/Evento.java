package com.giansiccardi.microservice.msvc_ubicacion.entity.pojo;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nombreEvento;

    @NotNull
    @PositiveOrZero
    private Integer cantidadEntradas;

    @NotNull
    @PositiveOrZero
    private Double precioEntradas;
}
