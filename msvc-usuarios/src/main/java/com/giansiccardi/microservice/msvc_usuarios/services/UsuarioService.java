package com.giansiccardi.microservice.msvc_usuarios.services;


import com.giansiccardi.microservice.msvc_usuarios.client.EventoClient;
import com.giansiccardi.microservice.msvc_usuarios.controller.UsuarioController;
import com.giansiccardi.microservice.msvc_usuarios.dto.CompraDto;
import com.giansiccardi.microservice.msvc_usuarios.entidades.Usuario;
import com.giansiccardi.microservice.msvc_usuarios.entidades.UsuarioEvento;
import com.giansiccardi.microservice.msvc_usuarios.entidades.pojo.Evento;
import com.giansiccardi.microservice.msvc_usuarios.repository.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    EventoClient client;


    private Logger log = LoggerFactory.getLogger(UsuarioService.class);


  /*  @Autowired
    private JWTservices jwTservices;


    public String generateToken(String userName){
        return jwTservices.generateToken(userName);
    }

    public void validarToken(String token){
        jwTservices.validateToken(token);
    }
*/
    @Transactional(readOnly = true)
    public List<Usuario> listarTodosUsuarios(){
        return usuarioRepository.findAll();
    }

  @Transactional
   public Usuario buscarPorId(Long id){
        Optional<Usuario> u= Optional.ofNullable(usuarioRepository.findById(id).orElse(null));

            if (u.isPresent()) {
                Usuario usuario = u.get();
                if (!usuario.getUsuarioEventos().isEmpty()) {
                    List<Long> ids = usuario.getUsuarioEventos().stream()
                            .map(UsuarioEvento::getUsuarioId)
                            .collect(Collectors.toList());
                    List<Evento> eventos = client.obtenerEventosPorIds(ids);
                    usuario.setEventos(eventos);
                    System.out.println("Eventos obtenidas: " + eventos);
                }
                return usuario;
        }

        return null;
   }

    @Transactional
    public Usuario crearUsuario(Usuario usuario){
        log.info("Creando usuario: {}", usuario);

        return usuarioRepository.save(usuario);
    }
    @Transactional
    public Optional<Usuario> byEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);

    }


public Optional<Evento>asignarEvento(CompraDto compra, Long id_user ){
        Optional<Usuario>u=usuarioRepository.findById(id_user);
        if(u.isPresent()){
            Evento eventoMsvc=client.buscarEventoPorid(compra.getEvento_id());
            Usuario usuario=u.get();
            UsuarioEvento usuarioEvento =new UsuarioEvento();
            usuarioEvento.setUsuarioId(eventoMsvc.getId());
            usuario.addEvento(usuarioEvento);
            Double total =eventoMsvc.getPrecioEntradas()*compra.getCantidadEntradas();
            Double saldoRestante=usuario.getSaldo()-total;
            usuario.setSaldo(total);
            usuarioRepository.save(usuario);

            return Optional.of(eventoMsvc);
        }

        return Optional.empty();
}


    public Optional<Evento>eliminarEvento(Evento evento ,Long idU){
        Optional<Usuario>u=usuarioRepository.findById(idU);
        if(u.isPresent()){
            Evento eventoMsvc=client.buscarEventoPorid(evento.getId());
            Usuario usuario=u.get();
            UsuarioEvento usuarioEvento= new UsuarioEvento();
            usuarioEvento.setUsuarioId(eventoMsvc.getId());
            usuario.addremove(usuarioEvento);
            usuarioRepository.save(usuario);
            return Optional.of(eventoMsvc);

        }
        return Optional.empty();
    }

    @Transactional
    public void eliminarUsuarioDeEvento(Long id){
usuarioRepository.eliminarEventoUbicacionPorid(id);
    }


    }


