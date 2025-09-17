 
package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    
 @Value("${equipos.img.upload-dir}")
    private String uploadDir;
    @Override
    @Transactional(readOnly = true)
    public Page<EquipoSalida> filtrarEquipos(String nombre, Short garantia, Integer ubicacionId, Integer categoriaId, Pageable pageable) {
        // Limitar garantía máxima a 24
        Short garantiaFiltrada = garantia != null ? (garantia > 24 ? (short) 24 : garantia) : null;
        return equipoRepository.filtrarEquipos(
            (nombre != null && !nombre.isBlank()) ? nombre : null,
            garantiaFiltrada,
            ubicacionId,
            categoriaId,
            pageable
        ).map(this::convertToDto);
    }

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
    public EquipoSalida guardar(EquipoGuardar equipoGuardar, org.springframework.web.multipart.MultipartFile imgFile) {
        if (equipoRepository.existsByNserie(equipoGuardar.getNserie())) {
            throw new IllegalArgumentException("Ya existe un equipo con el número de serie: " + equipoGuardar.getNserie());
        }

        Categoria categoria = categoriaRepository.findById(equipoGuardar.getCategoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        Ubicacion ubicacion = ubicacionRepository.findById(equipoGuardar.getUbicacionId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        // Guardar imagen en carpeta local y asignar solo el nombre
        String imgFileName = null;
        if (imgFile != null && !imgFile.isEmpty()) {
            try {
                // Log para debugging
                System.out.println("Upload dir configurado: " + uploadDir);
                
                java.nio.file.Path uploadPath = java.nio.file.Paths.get(uploadDir);
                System.out.println("Ruta absoluta donde se guardará: " + uploadPath.toAbsolutePath());
                
                if (!java.nio.file.Files.exists(uploadPath)) {
                    java.nio.file.Files.createDirectories(uploadPath);
                    System.out.println("Carpeta creada: " + uploadPath.toAbsolutePath());
                } else {
                    System.out.println("Carpeta ya existe: " + uploadPath.toAbsolutePath());
                }
                
                imgFileName = System.currentTimeMillis() + "_" + imgFile.getOriginalFilename();
                java.nio.file.Path filePath = uploadPath.resolve(imgFileName);
                
                System.out.println("Guardando archivo en: " + filePath.toAbsolutePath());
                imgFile.transferTo(filePath.toFile());
                System.out.println("Archivo guardado exitosamente: " + imgFileName);
                
            } catch (Exception e) {
                System.err.println("Error al guardar la imagen: " + e.getMessage());
                e.printStackTrace();
                throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
            }
        }
        equipoGuardar.setImg(imgFileName);

        Equipo equipo = equipoMapper.toEntity(equipoGuardar);
        equipo.setCategoria(categoria);
        equipo.setUbicacion(ubicacion);
        equipo.setFechaRegistro(LocalDateTime.now());

        Equipo equipoGuardado = equipoRepository.save(equipo);
        return convertToDto(equipoGuardado);
    }

    @Override
    @Transactional
    public Optional<EquipoSalida> actualizarConImagen(Integer id, EquipoModificar equipoModificar, MultipartFile imgFile) {
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
                    
                    // Manejar la imagen
                    String nombreImagen = equipoExistente.getImg(); // Mantener imagen actual por defecto
                    
                    if (imgFile != null && !imgFile.isEmpty()) {
                        try {
                            // Eliminar imagen anterior si existe
                            if (equipoExistente.getImg() != null && !equipoExistente.getImg().isEmpty()) {
                                Path imagenAnterior = Paths.get(uploadDir, equipoExistente.getImg());
                                System.out.println("Intentando eliminar imagen anterior: " + imagenAnterior.toAbsolutePath());
                                if (Files.exists(imagenAnterior)) {
                                    Files.delete(imagenAnterior);
                                    System.out.println("Imagen anterior eliminada exitosamente: " + equipoExistente.getImg());
                                } else {
                                    System.out.println("La imagen anterior no existe en el directorio: " + imagenAnterior.toAbsolutePath());
                                }
                            }
                            
                            // Guardar nueva imagen
                            String originalFilename = imgFile.getOriginalFilename();
                            nombreImagen = System.currentTimeMillis() + "_" + originalFilename;
                            Path uploadPath = Paths.get(uploadDir);
                            
                            if (!Files.exists(uploadPath)) {
                                Files.createDirectories(uploadPath);
                            }
                            
                            Path filePath = uploadPath.resolve(nombreImagen);
                            imgFile.transferTo(filePath.toFile());
                            System.out.println("Nueva imagen guardada: " + filePath.toAbsolutePath());
                            
                        } catch (IOException e) {
                            System.err.println("Error al manejar la imagen: " + e.getMessage());
                            throw new RuntimeException("Error al guardar la imagen: " + e.getMessage(), e);
                        }
                    }
                    
                    Equipo equipoActualizado = equipoMapper.toEntity(equipoModificar, equipoExistente);
                    equipoActualizado.setCategoria(categoria);
                    equipoActualizado.setUbicacion(ubicacion);
                    equipoActualizado.setFechaRegistro(equipoExistente.getFechaRegistro());
                    equipoActualizado.setImg(nombreImagen); // Establecer nombre de imagen (nueva o existente)
                    
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