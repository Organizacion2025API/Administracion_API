package org.esfe.ApiApexManagent.repositorios;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.esfe.ApiApexManagent.modelos.Ubicacion;

@Repository
public interface IUbicacionRepository extends JpaRepository<Ubicacion, Integer> {

    Page<Ubicacion> findByNombreUbicacionContaining(String nombreUbicacion, Pageable pageable);

    boolean existsByNombreUbicacion(String nombreUbicacion);
}