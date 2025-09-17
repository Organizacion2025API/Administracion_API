package org.esfe.ApiApexManagent.repositorios;

import org.esfe.ApiApexManagent.modelos.Equipo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IEquipoRepository extends JpaRepository<Equipo, Integer> {

    boolean existsByNserie(String nserie);
    
    Page<Equipo> findByNserieContainingAndNombreContainingAndModeloContaining(
        String nserie, String nombre, String modelo, Pageable pageable);
    
    @Query("SELECT COUNT(a) > 0 FROM AsignacionEquipo a WHERE a.equipo.id = :equipoId")
    boolean tieneAsignaciones(@Param("equipoId") Integer equipoId);
    
    @Query("SELECT e FROM Equipo e WHERE " +
           "(:nombre IS NULL OR LOWER(e.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) AND " +
           "(:garantia IS NULL OR e.garantia = :garantia) AND " +
           "(:ubicacionId IS NULL OR e.ubicacion.id = :ubicacionId) AND " +
           "(:categoriaId IS NULL OR e.categoria.id = :categoriaId)")
    Page<Equipo> filtrarEquipos(@Param("nombre") String nombre, 
                               @Param("garantia") Short garantia, 
                               @Param("ubicacionId") Integer ubicacionId, 
                               @Param("categoriaId") Integer categoriaId, 
                               Pageable pageable);
}