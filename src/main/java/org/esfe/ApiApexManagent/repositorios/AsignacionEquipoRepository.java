package org.esfe.ApiApexManagent.repositorios;

import org.esfe.ApiApexManagent.modelos.AsignacionEquipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AsignacionEquipoRepository extends JpaRepository<AsignacionEquipo, Integer> {
    List<AsignacionEquipo> findByEquipo_Id(Integer equipoId);
    List<AsignacionEquipo> findByPersonalId(Integer personalId);
}
