package com.giansiccardi.microservice.msvc_eventos.entidades;

import com.giansiccardi.microservice.msvc_eventos.entidades.pojo.Ubicacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="evento_id")
    private List<EventoUbicacion>eventoUbicacions;

    @Transient
    private List<Ubicacion>ubicacions;






    public void addUbicacion(EventoUbicacion eventoUbicacion){
        eventoUbicacions.add(eventoUbicacion);
    }

    public void removeUbicacion(EventoUbicacion eventoUbicacion){
        eventoUbicacions.remove(eventoUbicacion);
    }


}
