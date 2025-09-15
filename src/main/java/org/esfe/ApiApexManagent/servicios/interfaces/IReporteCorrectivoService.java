package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoCrearRequest;
import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoSalida;
import org.esfe.ApiApexManagent.modelos.ReporteCorrectivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IReporteCorrectivoService {
    ReporteCorrectivoSalida crearReporteCorrectivo(ReporteCorrectivoCrearRequest request, String nombrePersonal);
    Optional<ReporteCorrectivoSalida> obtenerPorSolicitudId(Integer solicitudId);
    Page<ReporteCorrectivoSalida> buscarPorFiltros(Short tipoMantenimiento, Short estado, Pageable pageable);
    List<ReporteCorrectivoSalida> listarTodos();
    boolean existeReporteParaSolicitud(Integer solicitudId);
}
