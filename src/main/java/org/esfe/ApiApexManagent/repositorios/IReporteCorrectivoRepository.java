package org.esfe.ApiApexManagent.repositorios;

import org.esfe.ApiApexManagent.modelos.ReporteCorrectivo;
import org.esfe.ApiApexManagent.modelos.Solicitud;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface IReporteCorrectivoRepository extends JpaRepository<ReporteCorrectivo, Integer> {
    Optional<ReporteCorrectivo> findBySolicitud(Solicitud solicitud);
    Optional<ReporteCorrectivo> findBySolicitudId(Integer solicitudId);
    boolean existsBySolicitudId(Integer solicitudId);
    Page<ReporteCorrectivo> findByTipoMantenimiento(Short tipoMantenimiento, Pageable pageable);
    Page<ReporteCorrectivo> findByEstado(Short estado, Pageable pageable);
    @Query("SELECT rc FROM ReporteCorrectivo rc WHERE (:tipoMantenimiento IS NULL OR rc.tipoMantenimiento = :tipoMantenimiento) AND (:estado IS NULL OR rc.estado = :estado)")
    Page<ReporteCorrectivo> buscarPorFiltros(@Param("tipoMantenimiento") Short tipoMantenimiento, @Param("estado") Short estado, Pageable pageable);
    long countByEstado(Short estado);
    @Query("SELECT rc.tipoMantenimiento, COUNT(rc) FROM ReporteCorrectivo rc GROUP BY rc.tipoMantenimiento")
    List<Object[]> countByTipoMantenimiento();
    @Query("SELECT FUNCTION('MONTH', rc.fechaCreacion), COUNT(rc) FROM ReporteCorrectivo rc WHERE FUNCTION('YEAR', rc.fechaCreacion) = FUNCTION('YEAR', CURRENT_DATE) GROUP BY FUNCTION('MONTH', rc.fechaCreacion) ORDER BY FUNCTION('MONTH', rc.fechaCreacion)")
    List<Object[]> countByMonth();
    @Query("SELECT rc.estado, COUNT(rc) FROM ReporteCorrectivo rc GROUP BY rc.estado")
    List<Object[]> countByEstadoAgrupado();
    long countByFechaCreacionBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    long countByEstadoAndFechaCreacionBetween(Short estado, LocalDateTime fechaInicio, LocalDateTime fechaFin);
    @Query("SELECT rc.tipoMantenimiento, COUNT(rc) FROM ReporteCorrectivo rc WHERE rc.fechaCreacion BETWEEN :fechaInicio AND :fechaFin GROUP BY rc.tipoMantenimiento")
    List<Object[]> countByTipoMantenimientoAndFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
    @Query("SELECT rc.estado, COUNT(rc) FROM ReporteCorrectivo rc WHERE rc.fechaCreacion BETWEEN :fechaInicio AND :fechaFin GROUP BY rc.estado")
    List<Object[]> countByEstadoAgrupadoAndFechaCreacionBetween(@Param("fechaInicio") LocalDateTime fechaInicio, @Param("fechaFin") LocalDateTime fechaFin);
}
