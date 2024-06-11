package com.giansiccardi.microservice.msvc_eventos.repository;

import com.giansiccardi.microservice.msvc_eventos.entidades.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EventoRepository extends JpaRepository<Evento,Long> {

Evento findByNombreEvento(String nombreEvento);

    @Modifying
    @Query("delete from EventoUbicacion eu where eu.ubicacionId=?1")//metodo para desasignar ubicacion de evento
    void eliminarEventoUbicacionPorid(Long id);

}
