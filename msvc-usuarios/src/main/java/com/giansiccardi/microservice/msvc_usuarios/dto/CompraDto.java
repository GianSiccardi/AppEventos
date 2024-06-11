package com.giansiccardi.microservice.msvc_usuarios.dto;

import com.giansiccardi.microservice.msvc_usuarios.entidades.pojo.Evento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompraDto {
    private Long  evento_id;
    private Integer cantidadEntradas;
}
