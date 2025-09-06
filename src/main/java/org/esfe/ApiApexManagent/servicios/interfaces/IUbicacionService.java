package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionGuardar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionModificar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IUbicacionService {
    
    Page<UbicacionSalida> listarUbicaciones(Pageable pageable);
    
    Page<UbicacionSalida> buscarPorNombre(String nombre, Pageable pageable);
    
    Optional<UbicacionSalida> obtenerPorId(Integer id);
    
    UbicacionSalida guardar(UbicacionGuardar ubicacionGuardar);
    
    Optional<UbicacionSalida> actualizar(Integer id, UbicacionModificar ubicacionModificar);
    
    boolean existePorNombre(String nombreUbicacion);
    
    boolean existePorId(Integer id);
}