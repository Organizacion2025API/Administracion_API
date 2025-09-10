package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.dtos.equipo.EquipoGuardar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoModificar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoSalida;
import org.esfe.ApiApexManagent.config.EquipoMapperConfig;
import org.esfe.ApiApexManagent.modelos.Categoria;
import org.esfe.ApiApexManagent.modelos.Equipo;
import org.esfe.ApiApexManagent.modelos.Ubicacion;
import org.esfe.ApiApexManagent.repositorios.ICategoriaRepository;
import org.esfe.ApiApexManagent.repositorios.IEquipoRepository;
import org.esfe.ApiApexManagent.repositorios.IUbicacionRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.IEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class EquipoService implements IEquipoService {

    @Autowired
    private IEquipoRepository equipoRepository;
    
    @Autowired
    private ICategoriaRepository categoriaRepository;
    
    @Autowired
    private IUbicacionRepository ubicacionRepository;
    
    @Autowired
    private EquipoMapperConfig.EquipoMapper equipoMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EquipoSalida> listarEquipos(Pageable pageable) {
        return equipoRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EquipoSalida> buscarEquipos(String nserie, String nombre, String modelo, Pageable pageable) {
        Page<Equipo> equipos = equipoRepository.findByNserieContainingAndNombreContainingAndModeloContaining(
            nserie, nombre, modelo, pageable);
        return equipos.map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EquipoSalida> obtenerPorId(Integer id) {
        return equipoRepository.findById(id)
                .map(this::convertToDto);
    }

    @Override
    @Transactional
    public EquipoSalida guardar(EquipoGuardar equipoGuardar) {
        if (equipoRepository.existsByNserie(equipoGuardar.getNserie())) {
            throw new IllegalArgumentException("Ya existe un equipo con el número de serie: " + equipoGuardar.getNserie());
        }
        
        Categoria categoria = categoriaRepository.findById(equipoGuardar.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        
        Ubicacion ubicacion = ubicacionRepository.findById(equipoGuardar.getUbicacionId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
        
        Equipo equipo = equipoMapper.toEntity(equipoGuardar);
        equipo.setCategoria(categoria);
        equipo.setUbicacion(ubicacion);
        equipo.setFechaRegistro(LocalDateTime.now());
        
        Equipo equipoGuardado = equipoRepository.save(equipo);
        return convertToDto(equipoGuardado);
    }

    @Override
    @Transactional
    public Optional<EquipoSalida> actualizar(Integer id, EquipoModificar equipoModificar) {
        return equipoRepository.findById(id)
                .map(equipoExistente -> {
                    if (!equipoExistente.getNserie().equals(equipoModificar.getNserie()) &&
                        equipoRepository.existsByNserie(equipoModificar.getNserie())) {
                        throw new IllegalArgumentException("Ya existe un equipo con el número de serie: " + equipoModificar.getNserie());
                    }
                    
                    Categoria categoria = categoriaRepository.findById(equipoModificar.getCategoriaId())
                            .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
                    
                    Ubicacion ubicacion = ubicacionRepository.findById(equipoModificar.getUbicacionId())
                            .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));
                    
                    Equipo equipoActualizado = equipoMapper.toEntity(equipoModificar, equipoExistente);
                    equipoActualizado.setCategoria(categoria);
                    equipoActualizado.setUbicacion(ubicacion);
                    equipoActualizado.setFechaRegistro(equipoExistente.getFechaRegistro());
                    
                    Equipo equipoGuardado = equipoRepository.save(equipoActualizado);
                    return convertToDto(equipoGuardado);
                });
    }

    @Override
    @Transactional
    public boolean eliminar(Integer id) {
        if (equipoRepository.existsById(id)) {
            if (equipoRepository.tieneAsignaciones(id)) {
                throw new IllegalStateException("No se puede eliminar el equipo porque tiene asignaciones activas");
            }
            equipoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNserie(String nserie) {
        return equipoRepository.existsByNserie(nserie);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneAsignaciones(Integer equipoId) {
        return equipoRepository.tieneAsignaciones(equipoId);
    }

    private EquipoSalida convertToDto(Equipo equipo) {
        return new EquipoSalida(
            equipo.getId(),
            equipo.getNserie(),
            equipo.getNombre(),
            equipo.getModelo(),
            equipo.getDescripcion(),
            equipo.getGarantia(),
            equipo.getImg(),
            equipo.getFechaRegistro(),
            equipo.getCategoria().getId(),
            equipo.getCategoria().getNombreCategoria(),
            equipo.getUbicacion().getId(),
            equipo.getUbicacion().getNombreUbicacion(),
            !equipo.getAsignacionEquipo().isEmpty()
        );
    }
}