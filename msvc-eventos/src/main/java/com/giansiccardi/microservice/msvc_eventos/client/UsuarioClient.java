package com.giansiccardi.microservice.msvc_eventos.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "msvc-usuarios", url = "msvc-usuarios:8001")
public interface UsuarioClient {

    @DeleteMapping("/usuarios/eliminar-evento-usuario/{id}")
    void eliminarEventoDeUsuarioCuandoSeEliminaElEvento(@PathVariable Long id );
}
