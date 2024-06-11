package com.giansiccardi.microservice.msvc_usuarios.entidades;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "usuario_evento")
public class UsuarioEvento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private Long usuarioId;



    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof UsuarioEvento)
            return false;
        UsuarioEvento u = (UsuarioEvento) o;
        return this.usuarioId !=null && this.usuarioId.equals(u.usuarioId);
    }

}
