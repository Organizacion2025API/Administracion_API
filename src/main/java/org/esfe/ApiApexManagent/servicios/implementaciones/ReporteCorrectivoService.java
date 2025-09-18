package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoCrearRequest;
import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoSalida;
import org.esfe.ApiApexManagent.modelos.ReporteCorrectivo;
import org.esfe.ApiApexManagent.modelos.Solicitud;
import org.esfe.ApiApexManagent.repositorios.IReporteCorrectivoRepository;
import org.esfe.ApiApexManagent.repositorios.ISolicitudRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.IReporteCorrectivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReporteCorrectivoService implements IReporteCorrectivoService {
    @Autowired
    private IReporteCorrectivoRepository reporteCorrectivoRepository;
    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Override
    public ReporteCorrectivoSalida crearReporteCorrectivo(ReporteCorrectivoCrearRequest request, String nombrePersonal) {
        Solicitud solicitud = solicitudRepository.findById(request.getSolicitudId()).orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        // Validar si ya existe un reporte para esta solicitud
        if (reporteCorrectivoRepository.existsBySolicitudId(solicitud.getId())) {
            throw new RuntimeException("Ya se realizó el mantenimiento correctivo del equipo. No se puede crear otro reporte para la misma solicitud.");
        }
        
        ReporteCorrectivo reporte = new ReporteCorrectivo();
        reporte.setSolicitud(solicitud);
        reporte.setObservacion(request.getObservacion());
        reporte.setTipoMantenimiento(request.getTipoMantenimiento());
        reporte.setEstado((short)3); // Finalizado
        reporte.setFechaCreacion(LocalDateTime.now());
        reporte.setNombrePersonal(nombrePersonal);
        ReporteCorrectivo guardado = reporteCorrectivoRepository.save(reporte);
        // Cambiar estado de la solicitud a finalizado
        solicitud.setEstado((short)3);
        solicitudRepository.save(solicitud);
        return toSalida(guardado);
    }

    @Override
    public Optional<ReporteCorrectivoSalida> obtenerPorSolicitudId(Integer solicitudId) {
        return reporteCorrectivoRepository.findBySolicitudId(solicitudId).map(this::toSalida);
    }

    @Override
    public Page<ReporteCorrectivoSalida> buscarPorFiltros(Short tipoMantenimiento, Short estado, Pageable pageable) {
        return reporteCorrectivoRepository.buscarPorFiltros(tipoMantenimiento, estado, pageable).map(this::toSalida);
    }

    @Override
    public List<ReporteCorrectivoSalida> listarTodos() {
        return reporteCorrectivoRepository.findAll().stream().map(this::toSalida).collect(Collectors.toList());
    }

    @Override
    public boolean existeReporteParaSolicitud(Integer solicitudId) {
        return reporteCorrectivoRepository.existsBySolicitudId(solicitudId);
    }

    private ReporteCorrectivoSalida toSalida(ReporteCorrectivo rc) {
        ReporteCorrectivoSalida dto = new ReporteCorrectivoSalida();
        dto.setId(rc.getId());
        dto.setSolicitudId(rc.getSolicitud() != null ? rc.getSolicitud().getId() : null);
        dto.setObservacion(rc.getObservacion());
        dto.setTipoMantenimiento(rc.getTipoMantenimiento());
        dto.setEstado(rc.getEstado());
        dto.setFechaCreacion(rc.getFechaCreacion());
        dto.setNombrePersonal(rc.getNombrePersonal());
        return dto;
    }
}
