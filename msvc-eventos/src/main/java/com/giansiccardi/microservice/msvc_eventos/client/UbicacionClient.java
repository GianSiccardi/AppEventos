package com.giansiccardi.microservice.msvc_eventos.client;


import com.giansiccardi.microservice.msvc_eventos.entidades.Evento;
import com.giansiccardi.microservice.msvc_eventos.entidades.pojo.Ubicacion;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-ubicacion", url = "msvc-ubicacion:8003") // Cambia la URL por la de tu microservicio de ubicaciones
public interface UbicacionClient {


    @GetMapping("/ubicaciones/{id}")
    Ubicacion obtenerUbicacion(@PathVariable Long id);

    @GetMapping("/ubicaciones/ubicacionesEventos")
    List<Ubicacion>obtenerEventosPorUbicacion(@RequestParam List<Long>ids);

    @PutMapping("/ubicaciones/agregarEvento/{idUbi}")
   Evento agregarEvento(@RequestBody Evento evento , @PathVariable Long idUbi);

    @PutMapping("/ubicaciones/{ubicacionId}/{eventoId}")
    void agregarEventoAUbicacion(@PathVariable Long ubicacionId, @PathVariable Long eventoId);


    @DeleteMapping("/ubicaciones/eliminar-evento-ubicacion/{id}")
    void eliminarEventoDeUbicacionCuandoSeEliminaElEvento(@PathVariable Long id );
}
