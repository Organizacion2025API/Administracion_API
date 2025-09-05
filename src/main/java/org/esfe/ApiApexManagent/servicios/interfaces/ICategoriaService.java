package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.dtos.categoria.CategoriaGuardar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaModificar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICategoriaService {
    Page<CategoriaSalida> listarCategorias(Pageable pageable);

    Page<CategoriaSalida> buscarPorNombre(String nombre, Pageable pageable);

    Optional<CategoriaSalida> obtenerPorId(Integer id);

    CategoriaSalida guardar(CategoriaGuardar categoriaGuardar);

    Optional<CategoriaSalida> actualizar(Integer id, CategoriaModificar categoriaModificar);

    boolean eliminar(Integer id);

    boolean existePorNombre(String nombreCategoria);

    boolean existePorId(Integer id);
}