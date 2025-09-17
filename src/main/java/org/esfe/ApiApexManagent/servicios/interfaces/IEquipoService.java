package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.dtos.equipo.EquipoGuardar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoModificar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

public interface IEquipoService {
    Page<EquipoSalida> listarEquipos(Pageable pageable);
    Page<EquipoSalida> buscarEquipos(String nserie, String nombre, String modelo, Pageable pageable);
    Page<EquipoSalida> filtrarEquipos(String nombre, Short garantia, Integer ubicacionId, Integer categoriaId, Pageable pageable);
    Optional<EquipoSalida> obtenerPorId(Integer id);
    EquipoSalida guardar(EquipoGuardar equipoGuardar, MultipartFile imgFile);
    Optional<EquipoSalida> actualizarConImagen(Integer id, EquipoModificar equipoModificar, MultipartFile imgFile);
    boolean eliminar(Integer id);
    boolean existePorNserie(String nserie);
    boolean tieneAsignaciones(Integer equipoId);
}