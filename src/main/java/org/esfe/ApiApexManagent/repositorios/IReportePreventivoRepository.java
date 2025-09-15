package org.esfe.ApiApexManagent.repositorios;

import org.esfe.ApiApexManagent.modelos.ReportePreventivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReportePreventivoRepository extends JpaRepository<ReportePreventivo, Integer> {
    Page<ReportePreventivo> findByCalendarioPreventivo_Equipo_Id(Integer equipoId, Pageable pageable);
    @Query("SELECT r FROM ReportePreventivo r WHERE (:equipoId IS NULL OR r.calendarioPreventivo.equipo.id = :equipoId) AND (:tipoMantenimiento IS NULL OR r.tipoMantenimiento = :tipoMantenimiento) AND (:estado IS NULL OR r.estado = :estado)")
    Page<ReportePreventivo> buscarPorFiltros(@Param("equipoId") Integer equipoId, @Param("tipoMantenimiento") Short tipoMantenimiento, @Param("estado") Short estado, Pageable pageable);
    List<ReportePreventivo> findByCalendarioPreventivo_Id(Integer calendarioPreventivoId);
}
