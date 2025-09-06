package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionGuardar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionModificar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionSalida;
import org.esfe.ApiApexManagent.config.UbicacionMapperConfig;
import org.esfe.ApiApexManagent.modelos.Ubicacion;
import org.esfe.ApiApexManagent.repositorios.IUbicacionRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.IUbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UbicacionService implements IUbicacionService {

    @Autowired
    private IUbicacionRepository ubicacionRepository;
    
    @Autowired
    private UbicacionMapperConfig ubicacionMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<UbicacionSalida> listarUbicaciones(Pageable pageable) {
        return ubicacionRepository.findAll(pageable)
                .map(ubicacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UbicacionSalida> buscarPorNombre(String nombre, Pageable pageable) {
        return ubicacionRepository.findByNombreUbicacionContaining(nombre, pageable)
                .map(ubicacionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UbicacionSalida> obtenerPorId(Integer id) {
        return ubicacionRepository.findById(id)
                .map(ubicacionMapper::toDto);
    }

    @Override
    @Transactional
    public UbicacionSalida guardar(UbicacionGuardar ubicacionGuardar) {
        // Verificar si ya existe una ubicación con el mismo nombre
        if (ubicacionRepository.existsByNombreUbicacion(ubicacionGuardar.getNombreUbicacion())) {
            throw new IllegalArgumentException("Ya existe una ubicación con el nombre: " + ubicacionGuardar.getNombreUbicacion());
        }
        
        Ubicacion ubicacion = ubicacionMapper.toEntity(ubicacionGuardar);
        Ubicacion ubicacionGuardada = ubicacionRepository.save(ubicacion);
        return ubicacionMapper.toDto(ubicacionGuardada);
    }

    @Override
    @Transactional
    public Optional<UbicacionSalida> actualizar(Integer id, UbicacionModificar ubicacionModificar) {
        return ubicacionRepository.findById(id)
                .map(ubicacionExistente -> {
                    // Verificar si el nuevo nombre ya existe en otra ubicación
                    if (!ubicacionExistente.getNombreUbicacion().equals(ubicacionModificar.getNombreUbicacion()) &&
                        ubicacionRepository.existsByNombreUbicacion(ubicacionModificar.getNombreUbicacion())) {
                        throw new IllegalArgumentException("Ya existe una ubicación con el nombre: " + ubicacionModificar.getNombreUbicacion());
                    }
                    
                    Ubicacion ubicacionActualizada = ubicacionMapper.toEntity(ubicacionModificar, ubicacionExistente);
                    Ubicacion ubicacionGuardada = ubicacionRepository.save(ubicacionActualizada);
                    return ubicacionMapper.toDto(ubicacionGuardada);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombreUbicacion) {
        return ubicacionRepository.existsByNombreUbicacion(nombreUbicacion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Integer id) {
        return ubicacionRepository.existsById(id);
    }
}