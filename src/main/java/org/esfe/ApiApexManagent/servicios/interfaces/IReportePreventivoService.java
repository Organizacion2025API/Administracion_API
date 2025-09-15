package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoCrearRequest;
import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoSalida;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IReportePreventivoService {
    ReportePreventivoSalida crearReportePreventivo(ReportePreventivoCrearRequest request, String nombrePersonal);
    Optional<ReportePreventivoSalida> obtenerPorId(Integer id);
    Page<ReportePreventivoSalida> buscarPorFiltros(Integer equipoId, Short tipoMantenimiento, Short estado, Pageable pageable);
    List<ReportePreventivoSalida> listarPorCalendario(Integer calendarioPreventivoId);
}
