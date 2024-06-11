package com.giansiccardi.springcloud.auth_eventos.controller;

import com.giansiccardi.springcloud.auth_eventos.dto.AuthRequest;
import com.giansiccardi.springcloud.auth_eventos.entidades.Usuario;
import com.giansiccardi.springcloud.auth_eventos.services.JWTservices;
import com.giansiccardi.springcloud.auth_eventos.services.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.GrantedAuthority;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class AuthController {

    @Autowired
    private AuthService usuarioService;

    private Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthenticationManager authenticationManager;


    @Autowired
    private JWTservices jwtServices;

    @PostMapping("/register")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body("Datos inválidos");
        }
        // Encriptar la contraseña antes de guardar
        //usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crearUsuario(usuario));
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest usuario) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getEmail(), usuario.getPassword())
        );

        if (authenticate.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authenticate.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            log.info("Generando token para usuario: {}, con roles: {}", userDetails.getUsername(), roles);
            return usuarioService.generateToken(usuario.getEmail(), roles);
        } else {
            throw new RuntimeException("Invalid access");
        }
    }

    @GetMapping("/validar")
    public String validarToken(@RequestParam("token") String token){
        usuarioService.validarToken(token);
        return "token valido";
    }
}
