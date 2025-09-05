package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.dtos.categoria.CategoriaGuardar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaModificar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaSalida;
import org.esfe.ApiApexManagent.config.CategoriaMapperConfig;
import org.esfe.ApiApexManagent.modelos.Categoria;
import org.esfe.ApiApexManagent.repositorios.ICategoriaRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private ICategoriaRepository categoriaRepository;
    
    @Autowired
    private CategoriaMapperConfig categoriaMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaSalida> listarCategorias(Pageable pageable) {
        return categoriaRepository.findAll(pageable)
                .map(categoriaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoriaSalida> buscarPorNombre(String nombre, Pageable pageable) {
        return categoriaRepository.findByNombreCategoriaContaining(nombre, pageable)
                .map(categoriaMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaSalida> obtenerPorId(Integer id) {
        return categoriaRepository.findById(id)
                .map(categoriaMapper::toDto);
    }

    @Override
    @Transactional
    public CategoriaSalida guardar(CategoriaGuardar categoriaGuardar) {
        // Verificar si ya existe una categoría con el mismo nombre
        if (categoriaRepository.existsByNombreCategoria(categoriaGuardar.getNombreCategoria())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaGuardar.getNombreCategoria());
        }
        
        Categoria categoria = categoriaMapper.toEntity(categoriaGuardar);
        Categoria categoriaGuardada = categoriaRepository.save(categoria);
        return categoriaMapper.toDto(categoriaGuardada);
    }

    @Override
    @Transactional
    public Optional<CategoriaSalida> actualizar(Integer id, CategoriaModificar categoriaModificar) {
        return categoriaRepository.findById(id)
                .map(categoriaExistente -> {
                    // Verificar si el nuevo nombre ya existe en otra categoría
                    if (!categoriaExistente.getNombreCategoria().equals(categoriaModificar.getNombreCategoria()) &&
                        categoriaRepository.existsByNombreCategoria(categoriaModificar.getNombreCategoria())) {
                        throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaModificar.getNombreCategoria());
                    }
                    
                    Categoria categoriaActualizada = categoriaMapper.toEntity(categoriaModificar, categoriaExistente);
                    Categoria categoriaGuardada = categoriaRepository.save(categoriaActualizada);
                    return categoriaMapper.toDto(categoriaGuardada);
                });
    }

    @Override
    @Transactional
    public boolean eliminar(Integer id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNombre(String nombreCategoria) {
        return categoriaRepository.existsByNombreCategoria(nombreCategoria);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorId(Integer id) {
        return categoriaRepository.existsById(id);
    }
}