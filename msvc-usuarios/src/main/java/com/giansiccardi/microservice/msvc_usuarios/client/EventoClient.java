package com.giansiccardi.microservice.msvc_usuarios.client;

import com.giansiccardi.microservice.msvc_usuarios.entidades.pojo.Evento;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="msvc-eventos" , url = "msvc-eventos:8002")
public interface EventoClient {

    @GetMapping("/{id}")
Evento buscarEventoPorid(@PathVariable Long id);

    @GetMapping("/porIds")
    List<Evento> obtenerEventosPorIds(@RequestParam List<Long> ids);
}