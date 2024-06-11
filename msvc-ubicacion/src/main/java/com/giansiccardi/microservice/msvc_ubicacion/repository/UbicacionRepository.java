package com.giansiccardi.microservice.msvc_ubicacion.repository;

import com.giansiccardi.microservice.msvc_ubicacion.entity.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion,Long> {

    Ubicacion findByNombreSalon(String nombre);

  @Modifying
 @Query("delete from UbicacionEvento ue where ue.eventoId=?1")
void eliminarUbicacionPorEventoId(Long id);
}
