package com.giansiccardi.microservice.msvc_ubicacion.service;

import com.giansiccardi.microservice.msvc_ubicacion.client.EventoClient;
import com.giansiccardi.microservice.msvc_ubicacion.entity.Ubicacion;
import com.giansiccardi.microservice.msvc_ubicacion.entity.UbicacionEvento;
import com.giansiccardi.microservice.msvc_ubicacion.entity.pojo.Evento;
import com.giansiccardi.microservice.msvc_ubicacion.repository.UbicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UbicacionService {

    @Autowired
    private UbicacionRepository ubicacionRepository;
  @Autowired
  private EventoClient eventoClient;


    @Transactional
    public Ubicacion crearUbicacion(Ubicacion ubicacion){
        Ubicacion ubicacionGuardado= ubicacionRepository.save(ubicacion);
        return ubicacionGuardado;
    }


    @Transactional(readOnly = true)
    public List<Ubicacion> listarTodasUbicaciones(){
        return ubicacionRepository.findAll();

    }

    @Transactional(readOnly = true)
    public Optional<Ubicacion> buscarPorId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }
        Optional<Ubicacion>e= ubicacionRepository.findById(id);
        return e;
    }

    @Transactional
    public Ubicacion actualizarUbicacion(Long id, Ubicacion ubicacion) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo.");
        }

        Ubicacion ubicacionExistente = ubicacionRepository.findById(id).orElseThrow(() -> new RuntimeException("Evento no encontrado"));


        ubicacionExistente.setCiudad(ubicacion.getCiudad());
        ubicacionExistente.setCapacidad(ubicacion.getCapacidad());
        ubicacionExistente.setNombreSalon(ubicacion.getNombreSalon());

        return ubicacionRepository.save(ubicacionExistente);
    }


    public void eliminarUbicacion(Long id){
        ubicacionRepository.deleteById(id);
        eventoClient.eliminarUbicacionDeEventoCuandoSeEliminaUbicacion(id);
    }

    @Transactional
    public Ubicacion buscarPorNombre(String name){
        if(name.isEmpty()){
            throw new IllegalArgumentException("El nombre no puede ser nulo.");
        }
        Ubicacion e= ubicacionRepository.findByNombreSalon(name);
        return e;
    }

public List<Ubicacion>listarPorIds(Iterable<Long>ids){

        return ubicacionRepository.findAllById(ids);
}

    public Optional<Ubicacion> agregarEvento(Long ubicacionId, Long eventoId) {
        Optional<Ubicacion> u = ubicacionRepository.findById(ubicacionId);
        if (u.isPresent()) {
            Ubicacion ubicacion = u.get();
            UbicacionEvento ubicacionEvento = new UbicacionEvento();
            ubicacionEvento.setEventoId(eventoId);
            ubicacion.getUbicacionEventoList().add(ubicacionEvento);
            ubicacionRepository.save(ubicacion);
            return Optional.of(ubicacion);
        }
        return Optional.empty();
    }
    public List<Ubicacion> obtenerEventosPorUbicacion(List<Long> ids) {
        List<Ubicacion> ubicaciones = ubicacionRepository.findAllById(ids);
        for (Ubicacion ubicacion : ubicaciones) {
            if (!ubicacion.getUbicacionEventoList().isEmpty()) {
                List<Long> eventoIds = ubicacion.getUbicacionEventoList().stream()
                        .map(UbicacionEvento::getEventoId)
                        .collect(Collectors.toList());
                List<Evento> eventos = eventoClient.obtenerEventosPorIds(eventoIds); // Método en el cliente Feign para obtener eventos por IDs
                ubicacion.setEventos(eventos);
            }
        }
        return ubicaciones;
    }


        public Optional<Ubicacion> porIdEvento(Long id) {
        Optional<Ubicacion> u = ubicacionRepository.findById(id);
        if (u.isPresent()) {
            Ubicacion ubicacion = u.get();
            if (!ubicacion.getUbicacionEventoList().isEmpty()) {
                List<Long> eventoIds = ubicacion.getUbicacionEventoList().stream()
                        .map(UbicacionEvento::getEventoId)
                        .collect(Collectors.toList());
                List<Evento> eventos = eventoClient.obtenerEventosPorIds(eventoIds); // Método en el cliente Feign para obtener eventos por IDs
                ubicacion.setEventos(eventos); // Asignar eventos a la ubicación
            }else {
                // Log to check if it's entering here
                System.out.println("Ubicacion " + ubicacion.getId() + " tiene una lista vacía de ubicacionEventoList.");
            }
            return Optional.of(ubicacion);
        }
        return Optional.empty();
    }

 public Optional<Evento>eliminarEvento(Evento evento ,Long idUbi){
        Optional<Ubicacion>u=ubicacionRepository.findById(idUbi);
        if(u.isPresent()){
            Evento eventoMsvc=eventoClient.buscarEventoPorid(evento.getId());
            Ubicacion ubicacion=u.get();
            UbicacionEvento ubicacionEvento= new UbicacionEvento();
            ubicacionEvento.setEventoId(eventoMsvc.getId());
            ubicacion.removeEvento(ubicacionEvento);
            ubicacionRepository.save(ubicacion);
            return Optional.of(eventoMsvc);

        }
        return Optional.empty();
 }

    @Transactional
    public void eliminarEventoDeUbicacion(Long id){
    ubicacionRepository.eliminarUbicacionPorEventoId(id);
    }




}
