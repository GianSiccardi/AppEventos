package com.giansiccardi.microservice.msvc_usuarios.entidades;

import com.giansiccardi.microservice.msvc_usuarios.entidades.pojo.Evento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotEmpty
    @Column(unique = true)
    private String userName;
    @NotBlank
    private String password;

    @NotBlank
    @Email
    @Column(unique = true)
    private String email;

    private Double saldo;


    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "role")
    private List<String> roles = new ArrayList<>();


    @OneToMany(cascade = CascadeType.ALL ,orphanRemoval = true)
    @JoinColumn(name = "id_usuario") // referencedColumnName added for clarity
    private List<UsuarioEvento>usuarioEventos;

    @Transient
    private List<Evento>eventos;

    public void addEvento(UsuarioEvento usuarioEvento){
        usuarioEventos.add(usuarioEvento);
    }


    public void addremove(UsuarioEvento usuarioEvento){
        usuarioEventos.remove(usuarioEvento);
    }
}
