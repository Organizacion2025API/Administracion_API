package org.esfe.ApiApexManagent.controladores;

import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoCrearRequest;
import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoSalida;
import org.esfe.ApiApexManagent.servicios.interfaces.IReporteCorrectivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes-correctivos")
public class ReporteCorrectivoController {
    private static final Logger logger = LoggerFactory.getLogger(ReporteCorrectivoController.class);
    @Autowired
    private IReporteCorrectivoService reporteCorrectivoService;

    // Crear y aceptar reporte correctivo (finaliza solicitud)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> crearReporte(@Valid @RequestBody ReporteCorrectivoCrearRequest request,
            Authentication authentication) {
        logger.info("Intentando crear reporte correctivo para solicitudId={}, usuario={}", request.getSolicitudId(),
                authentication.getName());
        try {
            String nombrePersonal = authentication.getName();
            ReporteCorrectivoSalida salida = reporteCorrectivoService.crearReporteCorrectivo(request, nombrePersonal);
            logger.info("Reporte correctivo creado exitosamente para solicitudId={}", request.getSolicitudId());
            return ResponseEntity.status(HttpStatus.CREATED).body(salida);
        } catch (Exception e) {
            logger.error("Error al crear reporte correctivo para solicitudId={}: {}", request.getSolicitudId(),
                    e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // Obtener reporte por solicitud
    @GetMapping("/solicitud/{solicitudId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> obtenerPorSolicitud(@PathVariable Integer solicitudId) {
        logger.info("Consultando reporte correctivo para solicitudId={}", solicitudId);
        Optional<ReporteCorrectivoSalida> reporte = reporteCorrectivoService.obtenerPorSolicitudId(solicitudId);
        if (reporte.isPresent()) {
            logger.info("Reporte correctivo encontrado para solicitudId={}", solicitudId);
            return ResponseEntity.ok(reporte.get());
        } else {
            logger.warn("No se encontró reporte correctivo para solicitudId={}", solicitudId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Reporte no encontrado para la solicitud " + solicitudId);
        }
    }

    // Listar todos los reportes (paginado y filtrado)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<Page<ReporteCorrectivoSalida>> listarReportes(
            @RequestParam(required = false) Short tipoMantenimiento,
            @RequestParam(required = false) Short estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReporteCorrectivoSalida> reportes = reporteCorrectivoService.buscarPorFiltros(tipoMantenimiento, estado,
                pageable);
        return ResponseEntity.ok(reportes);
    }

    // Listar todos los reportes (sin paginar)
    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<List<ReporteCorrectivoSalida>> listarTodos() {
        return ResponseEntity.ok(reporteCorrectivoService.listarTodos());
    }
}
