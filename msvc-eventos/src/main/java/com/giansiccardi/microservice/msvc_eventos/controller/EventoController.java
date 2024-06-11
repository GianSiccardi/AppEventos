package com.giansiccardi.microservice.msvc_eventos.controller;

import com.giansiccardi.microservice.msvc_eventos.entidades.Evento;
import com.giansiccardi.microservice.msvc_eventos.entidades.pojo.Ubicacion;
import com.giansiccardi.microservice.msvc_eventos.services.EventoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/eventos")
public class EventoController {
    private static final Logger logger = LoggerFactory.getLogger(EventoController.class);
    @Autowired
    private EventoService eventoService;

    @PostMapping
    public ResponseEntity<?> crearEvento(@Valid @RequestBody Evento evento, BindingResult result) {
        if (evento.getNombreEvento().isEmpty() && evento.getCantidadEntradas() == null && evento.getPrecioEntradas() == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "Algunos campos estan vacios"));
        }
        if(evento.getPrecioEntradas()<=0 || evento.getCantidadEntradas() <=149 ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "el precio no puede ser 0 o la cantidad de entradas debe ser superior a 150"));
        }


        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.crearEvento(evento));
    }

    @GetMapping
    public Map<String, List<Evento>> listarEventos() {
        return Collections.singletonMap("eventos", eventoService.listarTodosEventos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEventoPorid(@PathVariable Long id) {

        Optional<Evento> e = eventoService.porIdUbicacion(id);
        if (e.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", "No se encontro el evento con ese id"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(e);

    }


    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarEvento(@PathVariable Long id, @RequestBody Evento eventoNuevo) {
        try {
            Evento eventoActualizado = eventoService.actualizarEvento(id, eventoNuevo);
            return ResponseEntity.ok(eventoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Hay un campo mal puesto"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {

        Optional<Evento> o = eventoService.buscarPorId(id);
        if (o.isPresent()) {
            eventoService.eliminarEvento(id);
            return ResponseEntity.ok("eliminado");
        }
        return ResponseEntity.badRequest().body("no se encontro el usuario con ese id");
    }


    @GetMapping("/byname/{name}")
    public ResponseEntity<?> buscarPorNombre(@PathVariable String name) {
        if (name.isEmpty()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "el nombre no puede ser nulo"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(eventoService.buscarPorNombre(name));

    }

    @PutMapping("/asignar-ubicacion/{eventoId}")
    public ResponseEntity<?> asingarUbicacion(@RequestBody Ubicacion ubicacion, @PathVariable Long eventoId) {


        Optional<Ubicacion> u = eventoService.asignarUbicacion(ubicacion, eventoId);
        if (u.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(u.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe la ubicacion o hubo un error en la comunciacion"));
    }

    @DeleteMapping("/eliminar-ubi/{eventoId}")
    public ResponseEntity<?> eliminarUbicacion(@RequestBody Ubicacion ubicacion, @PathVariable Long eventoId) {

        Optional<Ubicacion> u = eventoService.eliminarUbicacion(ubicacion, eventoId);
        if (u.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body("eliminado");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No existe la ubicacion o hubo un error en la comunciacion"));

    }

    @DeleteMapping("/eliminar-ubicacion/{id}")
    public ResponseEntity<?> eliminarUbicacionDeEventoCuandoSeEliminaUbicacion(@PathVariable Long id) {
        logger.info("Solicitud de eliminación de ubicación recibida para ID: {}", id);
        eventoService.eliminarUbicacionDeEvento(id);
        return ResponseEntity.ok().body("eliminado");
    }

    @GetMapping("/porIds")
    public List<Evento> obtenerUbicacionPorEvento(@RequestParam List<Long> ids) {
        List<Evento> eventos = eventoService.obtenerEventosPorIds(ids);
        System.out.println("Eventos encontrados: " + eventos);
        return eventos;
    }
}