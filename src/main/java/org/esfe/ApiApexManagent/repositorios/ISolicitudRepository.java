package org.esfe.ApiApexManagent.repositorios;

import org.esfe.ApiApexManagent.modelos.Solicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISolicitudRepository extends JpaRepository<Solicitud, Integer> {
    List<Solicitud> findByAsignacionEquipo_PersonalId(Integer personalId);
    List<Solicitud> findByEstado(short estado);
    List<Solicitud> findByAsignacionEquipo_Id(Integer asignacionEquipoId);
}
