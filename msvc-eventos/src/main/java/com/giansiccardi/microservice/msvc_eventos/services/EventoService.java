package com.giansiccardi.microservice.msvc_eventos.services;

import com.giansiccardi.microservice.msvc_eventos.client.UbicacionClient;
import com.giansiccardi.microservice.msvc_eventos.client.UsuarioClient;
import com.giansiccardi.microservice.msvc_eventos.entidades.Evento;
import com.giansiccardi.microservice.msvc_eventos.entidades.EventoUbicacion;
import com.giansiccardi.microservice.msvc_eventos.entidades.pojo.Ubicacion;
import com.giansiccardi.microservice.msvc_eventos.repository.EventoRepository;
import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventoService {

@Autowired
 private EventoRepository eventoRepository;

@Autowired
private UbicacionClient ubicacionClient;

@Autowired
private UsuarioClient usuarioClient;


    private static final Logger logger = LoggerFactory.getLogger(EventoService.class);
@Transactional
 public Evento crearEvento(Evento evento){

    Evento eventoGuardado=eventoRepository.save(evento);
    return eventoGuardado;
}


@Transactional(readOnly = true)
public List<Evento> listarTodosEventos(){

    return eventoRepository.findAll();

}

@Transactional(readOnly = true)
public Optional<Evento>buscarPorId(Long id){
    if (id == null) {
        throw new IllegalArgumentException("El ID no puede ser nulo.");
    }
    Optional<Evento>e=eventoRepository.findById(id);
    return e;
}

    @Transactional
    public Evento actualizarEvento(Long id, Evento eventoNuevo) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }

        Evento eventoExistente = eventoRepository.findById(id).orElseThrow(() -> new RuntimeException("Evento no encontrado"));


        eventoExistente.setNombreEvento(eventoNuevo.getNombreEvento());
        eventoExistente.setCantidadEntradas(eventoNuevo.getCantidadEntradas());
        eventoExistente.setPrecioEntradas(eventoNuevo.getPrecioEntradas());
        //eventoExistente.setUbicacion(eventoNuevo.getUbicacion());

        return eventoRepository.save(eventoExistente);
    }


    public void eliminarEvento(Long id) {
        eventoRepository.deleteById(id);
        try {
            logger.info("Eliminando ubicación del evento con ID: {}", id);
            usuarioClient.eliminarEventoDeUsuarioCuandoSeEliminaElEvento(id);
            ubicacionClient.eliminarEventoDeUbicacionCuandoSeEliminaElEvento(id);
        } catch (FeignException e) {
            logger.error("Error al eliminar ubicación del evento con ID: {}", id, e);
            throw e;
        }
    }



@Transactional
    public Evento buscarPorNombre(String name){
    if(name.isEmpty()){
        throw new IllegalArgumentException("El nombre no puede ser nulo.");
    }
    Evento e=eventoRepository.findByNombreEvento(name);
    return e;
}

public Optional<Ubicacion>asignarUbicacion(Ubicacion ubicacion ,Long id){
Optional<Evento>e=eventoRepository.findById(id);
if(e.isPresent()){
    Ubicacion ubicacionMsvc= ubicacionClient.obtenerUbicacion(ubicacion.getId());
    Evento evento=e.get();
    if(evento.getCantidadEntradas()>ubicacionMsvc.getCapacidad()){
        throw new IllegalArgumentException("La capacidad de la ubicación es menor que la cantidad de entradas del evento");
    }


    EventoUbicacion eventoUbicacion= new EventoUbicacion();
    eventoUbicacion.setUbicacionId(ubicacionMsvc.getId());
    evento.addUbicacion(eventoUbicacion);
    eventoRepository.save(evento);

    ubicacionClient.agregarEventoAUbicacion(ubicacion.getId(), evento.getId());

    return Optional.of(ubicacionMsvc);
}
    return Optional.empty();
}


    public Optional<Ubicacion>eliminarUbicacion(Ubicacion ubicacion ,Long id){
        Optional<Evento>e=eventoRepository.findById(id);
        if(e.isPresent()){
            Ubicacion ubicacionMsvc= ubicacionClient.obtenerUbicacion(ubicacion.getId());
            Evento evento=e.get();
            EventoUbicacion eventoUbicacion= new EventoUbicacion();
            eventoUbicacion.setUbicacionId(ubicacionMsvc.getId());
            evento.removeUbicacion(eventoUbicacion);
            eventoRepository.save(evento);
            return Optional.of(ubicacionMsvc);
        }
        return Optional.empty();
    }


    public Optional<Evento> porIdUbicacion(Long id) {
        Optional<Evento> u = eventoRepository.findById(id);
        System.out.println("Evento encontrado: " + u);
        if (u.isPresent()) {
            Evento evento = u.get();
            if (!evento.getEventoUbicacions().isEmpty()) {
                List<Long> ids = evento.getEventoUbicacions().stream()
                        .map(EventoUbicacion::getUbicacionId)
                        .collect(Collectors.toList());
                List<Ubicacion> ubicacions = ubicacionClient.obtenerEventosPorUbicacion(ids);
                evento.setUbicacions(ubicacions);
                System.out.println("Ubicaciones obtenidas: " + ubicacions);
            }
            return Optional.of(evento);
        }
        return Optional.empty();
    }





    @Transactional(readOnly = true)
    public List<Evento> obtenerEventosPorIds(List<Long> ids) {
        return eventoRepository.findAllById(ids);
    }


    @Transactional
    public void eliminarUbicacionDeEvento(Long id){

    eventoRepository.eliminarEventoUbicacionPorid(id);
}

}
