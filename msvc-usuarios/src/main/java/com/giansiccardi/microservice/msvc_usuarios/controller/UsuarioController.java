package com.giansiccardi.microservice.msvc_usuarios.controller;

import com.giansiccardi.microservice.msvc_usuarios.dto.CompraDto;
import com.giansiccardi.microservice.msvc_usuarios.entidades.Usuario;
import com.giansiccardi.microservice.msvc_usuarios.entidades.pojo.Evento;
import com.giansiccardi.microservice.msvc_usuarios.services.UsuarioService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    private Logger log = LoggerFactory.getLogger(UsuarioController.class);



    @GetMapping
    public Map<String,List<Usuario>> listar(){
        return Collections.singletonMap("usuarios", usuarioService.listarTodosUsuarios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?>buscarUsuarioPorId(@PathVariable Long id){
        Optional<Usuario>usuario= Optional.ofNullable(usuarioService.buscarPorId(id));
        if(!usuario.isPresent()){
            return ResponseEntity.notFound().build();
        }
            return ResponseEntity.ok().body(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?>editar(@Valid @RequestBody Usuario usuario , BindingResult result, @PathVariable Long id){

        Optional<Usuario> o= Optional.ofNullable(usuarioService.buscarPorId(id));

        if(o.isPresent()){
            Usuario usuarioNuevo=o.get();
            if(!usuario.getEmail().equalsIgnoreCase(usuarioNuevo.getEmail()) &&
                    !usuario.getEmail().isEmpty() &&
                    usuarioService.byEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("error","ya existe un usuario con ese email"));
            }
            usuarioNuevo.setUserName(usuario.getUserName());
            usuarioNuevo.setEmail(usuario.getEmail());
            usuarioNuevo.setPassword(passwordEncoder.encode(usuario.getPassword()));

            usuarioService.crearUsuario(usuarioNuevo);
            return ResponseEntity.ok(usuarioNuevo);
        }
        return ResponseEntity.notFound().build();

}


@DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id){
        Optional<Usuario>u= Optional.ofNullable(usuarioService.buscarPorId(id));
        if(!u.isPresent()){

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no se encontro el usuario con ese id");
        }
        return ResponseEntity.ok().body("ELIMINADO");

}

    @PutMapping("/comprar/{usuarioId}")
    public ResponseEntity<?>comprarEntradaEvento(@RequestBody CompraDto compraDto, @PathVariable Long usuarioId ){
       if(compraDto.getCantidadEntradas()<=0){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La cantidad de entradas no puede ser 0 o menor");
       }

        Optional<Evento>e=usuarioService.asignarEvento(compraDto,usuarioId);
    if (e.isPresent()) {
        return ResponseEntity.status(HttpStatus.CREATED).body(e.get());
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe la ubicacion o hubo un error en la comuncion"));
}


    @DeleteMapping("/cancelacion/{userId}")
    public ResponseEntity<?> eliminarEvento(@RequestBody Evento  evento, @PathVariable Long userId) {

        Optional<Evento> u = usuarioService.eliminarEvento(evento, userId);
        if (u.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("eliminado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe el evento o hubo un error en la comunciacion"));

    }

    @DeleteMapping("/eliminar-evento-usuario/{id}")
    public ResponseEntity<?>eliminarEventoDeUsuarioCuandoSeEliminaElEvento(@PathVariable Long id ){
        usuarioService.eliminarUsuarioDeEvento(id);
        return ResponseEntity.ok().body("eliminado");
    }

   /* @GetMapping("/authorized")
    public Map<String,Object>authorized(@RequestParam(name="code") String code){
        return Collections.singletonMap("code",code);
    }

    @GetMapping("/login")
    public ResponseEntity<?>loginByEmail(@RequestParam(name="email") String email){
        Optional<Usuario>u = usuarioService.byEmail(email);
        if(u.isPresent()){
            return ResponseEntity.ok().body(u.get());
        }
        return ResponseEntity.notFound().build();

    }*/
/*@PostMapping("/token")
    public String getToken(@RequestBody AuthRequest usuario){
    Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword()));
    if (authenticate.isAuthenticated()) {
        return usuarioService.generateToken(usuario.getEmail());
    } else {
        throw new RuntimeException("invalid access");
    }
    }

    @GetMapping("/valida")
    public String validarToken(@RequestParam("token") String token){
    usuarioService.validarToken(token);
    return "token valido";
    }
*/
    }





