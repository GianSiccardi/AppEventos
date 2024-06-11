package com.giansiccardi.microservice.msvc_usuarios.repository;


import com.giansiccardi.microservice.msvc_usuarios.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    @Modifying
    @Query("delete from UsuarioEvento eu where eu.usuarioId=?1")//metodo para desasignar ubicacion de evento
    void eliminarEventoUbicacionPorid(Long id);

}
