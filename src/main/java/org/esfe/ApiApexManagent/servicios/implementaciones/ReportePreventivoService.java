package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoCrearRequest;
import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoSalida;
import org.esfe.ApiApexManagent.modelos.ReportePreventivo;
import org.esfe.ApiApexManagent.modelos.CalendarioPreventivo;
import org.esfe.ApiApexManagent.repositorios.IReportePreventivoRepository;
import org.esfe.ApiApexManagent.repositorios.ICalendarioPreventivoRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.IReportePreventivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReportePreventivoService implements IReportePreventivoService {
    @Autowired
    private IReportePreventivoRepository reportePreventivoRepository;
    @Autowired
    private ICalendarioPreventivoRepository calendarioPreventivoRepository;

    @Override
    public ReportePreventivoSalida crearReportePreventivo(ReportePreventivoCrearRequest request, String nombrePersonal) {
        CalendarioPreventivo calendario = calendarioPreventivoRepository.findById(request.getCalendarioPreventivoId())
                .orElseThrow(() -> new RuntimeException("Calendario preventivo no encontrado"));
        
        // Validar si ya existe un reporte para este calendario preventivo
        List<ReportePreventivo> reportesExistentes = reportePreventivoRepository.findByCalendarioPreventivo_Id(request.getCalendarioPreventivoId());
        if (!reportesExistentes.isEmpty()) {
            throw new RuntimeException("Ya se realizó el mantenimiento preventivo del equipo. No se puede crear otro reporte para el mismo calendario preventivo.");
        }
        
    ReportePreventivo reporte = new ReportePreventivo();
    reporte.setCalendarioPreventivo(calendario);
    reporte.setPersonal(nombrePersonal); // Guardar el username directamente
    reporte.setObservacion(request.getObservacion());
    reporte.setTipoMantenimiento(request.getTipoMantenimiento());
    reporte.setEstado((short) 3); // Completado
    reporte.setFechaAtencion(LocalDateTime.now());
    ReportePreventivo guardado = reportePreventivoRepository.save(reporte);
    // Cambiar estado del calendario a completado
    calendario.setEstadoMantenimiento((short) 3);
    calendarioPreventivoRepository.save(calendario);
    return toSalida(guardado);
    }

    @Override
    public Optional<ReportePreventivoSalida> obtenerPorId(Integer id) {
        return reportePreventivoRepository.findById(id).map(this::toSalida);
    }

    @Override
    public Page<ReportePreventivoSalida> buscarPorFiltros(Integer equipoId, Short tipoMantenimiento, Short estado, Pageable pageable) {
        return reportePreventivoRepository.buscarPorFiltros(equipoId, tipoMantenimiento, estado, pageable).map(this::toSalida);
    }

    @Override
    public List<ReportePreventivoSalida> listarPorCalendario(Integer calendarioPreventivoId) {
        return reportePreventivoRepository.findByCalendarioPreventivo_Id(calendarioPreventivoId).stream().map(this::toSalida).collect(Collectors.toList());
    }

    private ReportePreventivoSalida toSalida(ReportePreventivo rp) {
        ReportePreventivoSalida dto = new ReportePreventivoSalida();
        dto.setId(rp.getId());
        dto.setCalendarioPreventivoId(rp.getCalendarioPreventivo() != null ? rp.getCalendarioPreventivo().getId() : null);
        dto.setObservacion(rp.getObservacion());
        dto.setTipoMantenimiento(rp.getTipoMantenimiento());
        dto.setEstado(rp.getEstado());
        dto.setFechaAtencion(rp.getFechaAtencion());
    dto.setNombrePersonal(rp.getPersonal());
        dto.setEquipoId(rp.getCalendarioPreventivo() != null && rp.getCalendarioPreventivo().getEquipo() != null ? rp.getCalendarioPreventivo().getEquipo().getId() : null);
        return dto;
    }
}
