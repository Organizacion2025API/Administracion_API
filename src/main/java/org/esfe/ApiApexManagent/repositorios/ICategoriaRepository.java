package org.esfe.ApiApexManagent.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.esfe.ApiApexManagent.modelos.Categoria;

@Repository
public interface ICategoriaRepository extends JpaRepository<Categoria, Integer> {

    Page<Categoria> findByNombreCategoriaContaining(String nombreCategoria, Pageable pageable);

    boolean existsByNombreCategoria(String nombreCategoria);
}
