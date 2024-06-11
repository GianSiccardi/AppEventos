package com.giansiccardi.microservice.msvc_ubicacion.controller;

import com.giansiccardi.microservice.msvc_ubicacion.entity.Ubicacion;
import com.giansiccardi.microservice.msvc_ubicacion.entity.pojo.Evento;
import com.giansiccardi.microservice.msvc_ubicacion.service.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/ubicaciones")
public class UbicacionController {


    @Autowired
    private UbicacionService ubicacionService;

    @PostMapping
    public ResponseEntity<?> crearEvento(@Valid @RequestBody Ubicacion ubicacion , BindingResult result){
        if(ubicacion.getNombreSalon().isEmpty() && ubicacion.getCapacidad()==null && ubicacion.getCiudad().isEmpty()){

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error","Algunos campos estan vacios"));
        }
        ubicacionService.crearUbicacion(ubicacion);
        return ResponseEntity.status(HttpStatus.CREATED).body(ubicacion);
    }

    @GetMapping
    public Map<String, List<Ubicacion>> ListarUbicacion(){
        return Collections.singletonMap("ubicacion",ubicacionService.listarTodasUbicaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerUbicacionConEventos(@PathVariable Long id) {
        Optional<Ubicacion> ubicacion = ubicacionService.porIdEvento(id);
        if (ubicacion.isPresent()) {
            return ResponseEntity.ok(ubicacion.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se encontró la ubicación con ese ID"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarUbicacion (@PathVariable Long id , @RequestBody Ubicacion ubicacionNueva){
        try {
            Ubicacion ubicacionActualizado = ubicacionService.actualizarUbicacion(id, ubicacionNueva);
            return ResponseEntity.ok(ubicacionActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Hay un campo mal puesto"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?>eliminar(@PathVariable Long id){

        Optional<Ubicacion> o=ubicacionService.buscarPorId(id);
        if(o.isPresent()){
            ubicacionService.eliminarUbicacion(id);
            return ResponseEntity.ok("eliminado");
        }
        return ResponseEntity.badRequest().body("no se encontro el usuario con ese id");
    }

    @PutMapping("/{ubicacionId}/{eventoId}")
    public ResponseEntity<?> agregarEventoAUbicacion(@PathVariable Long ubicacionId, @PathVariable Long eventoId) {        Optional<Ubicacion> u = ubicacionService.agregarEvento(ubicacionId, eventoId);
        if (u.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(u.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se encontró la ubicación o hubo un error en la comunicación"));
    }


    @GetMapping("/byname/{name}")
    public ResponseEntity<?>buscarPorNombre(@PathVariable String name){
        if(name.isEmpty()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("error","el nombre no puede ser nulo"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(ubicacionService.buscarPorNombre(name));

    }

    @GetMapping("/ubicacionesEventos")
    public List<Ubicacion> obtenerEventosPorUbicacion(@RequestParam List<Long> ids) {
        List<Ubicacion> ubicaciones = ubicacionService.obtenerEventosPorUbicacion(ids);
        System.out.println("Ubicaciones encontradas: " + ubicaciones);
        return ubicaciones;
    }

    @DeleteMapping("/eliminar-evento/{idUbi}")
    public ResponseEntity<?> eliminarEvento(@RequestBody Evento evento, @PathVariable Long idUbi) {
        Optional<Ubicacion>u=ubicacionService.buscarPorId(idUbi);
        Optional<Evento>e=ubicacionService.eliminarEvento(evento , idUbi);
  if(e.isPresent()) {
      return ResponseEntity.ok().body("evento eliminado de esta ubicacion "+ u.get().getNombreSalon() );
  }
  return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/eliminar-evento-ubicacion/{id}")
    public ResponseEntity<?>eliminarEventoDeUbicacionCuandoSeEliminaElEvento(@PathVariable Long id ){
        ubicacionService.eliminarEventoDeUbicacion(id);
        return ResponseEntity.ok().body("eliminado");
  }


    }
