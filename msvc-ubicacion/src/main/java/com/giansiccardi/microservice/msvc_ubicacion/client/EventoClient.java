package com.giansiccardi.microservice.msvc_ubicacion.client;

import com.giansiccardi.microservice.msvc_ubicacion.entity.pojo.Evento;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="msvc-eventos" , url = "msvc-ubicacion:8002")
public interface EventoClient {


    @DeleteMapping("/eventos/eliminar-ubicacion/{id}")
    void eliminarUbicacionDeEventoCuandoSeEliminaUbicacion(@PathVariable Long id);

    @GetMapping("/eventos/{id}")
    Evento buscarEventoPorid(@PathVariable Long id);

    @GetMapping("/eventos/porIds")
    List<Evento> obtenerEventosPorIds(@RequestParam List<Long> ids);



    @DeleteMapping("/eventos/{id}")
   void  eliminar(@PathVariable Long id);
}
