package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.dtos.equipo.EquipoGuardar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoModificar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IEquipoService {
    
    Page<EquipoSalida> listarEquipos(Pageable pageable);
    
    Page<EquipoSalida> buscarEquipos(String nserie, String nombre, String modelo, Pageable pageable);
    
    Optional<EquipoSalida> obtenerPorId(Integer id);
    
    EquipoSalida guardar(EquipoGuardar equipoGuardar);
    
    Optional<EquipoSalida> actualizar(Integer id, EquipoModificar equipoModificar);
    
    boolean eliminar(Integer id);
    
    boolean existePorNserie(String nserie);
    
    boolean tieneAsignaciones(Integer equipoId);
}