package org.esfe.ApiApexManagent.repositorios;

import org.esfe.ApiApexManagent.modelos.CalendarioPreventivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICalendarioPreventivoRepository extends JpaRepository<CalendarioPreventivo, Integer> {
    Page<CalendarioPreventivo> findByEstadoMantenimiento(Short estado, Pageable pageable);
    Page<CalendarioPreventivo> findByEquipo_NombreContainingIgnoreCaseAndEstadoMantenimiento(String nombre, Short estado, Pageable pageable);
    Page<CalendarioPreventivo> findByEquipo_NombreContainingIgnoreCase(String nombre, Pageable pageable);
}
