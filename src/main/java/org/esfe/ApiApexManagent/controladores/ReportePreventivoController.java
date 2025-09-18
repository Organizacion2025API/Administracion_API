package org.esfe.ApiApexManagent.controladores;

import org.esfe.ApiApexManagent.controladores.base.BaseController;
import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoCrearRequest;
import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoSalida;
import org.esfe.ApiApexManagent.servicios.interfaces.IReportePreventivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes-preventivos")
public class ReportePreventivoController extends BaseController {
    @Autowired
    private IReportePreventivoService reportePreventivoService;

    // Crear y aceptar reporte preventivo (finaliza mantenimiento)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> crearReporte(@Valid @RequestBody ReportePreventivoCrearRequest request,
            Authentication authentication, HttpServletRequest httpRequest) {
        try {
            System.out.println("[REPORTE-PREVENTIVO-DEBUG] Iniciando creación de reporte preventivo");
            
            // Extraer personalId usando el método utilitario
            Optional<Integer> personalIdOpt = extraerPersonalId(authentication, httpRequest);
            if (personalIdOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("No se pudo determinar el ID del usuario desde el token JWT");
            }
            
            Integer personalId = personalIdOpt.get();
            System.out.println("[REPORTE-PREVENTIVO-DEBUG] PersonalId final: " + personalId);
            
            // Convertir personalId a String para el servicio (manteniendo compatibilidad)
            String nombrePersonal = personalId.toString();
            
            ReportePreventivoSalida salida = reportePreventivoService.crearReportePreventivo(request, nombrePersonal);
            return ResponseEntity.status(HttpStatus.CREATED).body(salida);
        } catch (Exception e) {
            System.err.println("[REPORTE-PREVENTIVO-ERROR] Error al crear reporte: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // Obtener reporte por id
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        var reporte = reportePreventivoService.obtenerPorId(id);
        if (reporte.isPresent()) {
            return ResponseEntity.ok(reporte.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reporte preventivo no encontrado");
        }
    }

    // Listar reportes con filtros (equipo, tipo, estado, paginado)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<Page<ReportePreventivoSalida>> listarReportes(
            @RequestParam(required = false) Integer equipoId,
            @RequestParam(required = false) Short tipoMantenimiento,
            @RequestParam(required = false) Short estado,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ReportePreventivoSalida> reportes = reportePreventivoService.buscarPorFiltros(equipoId, tipoMantenimiento,
                estado, pageable);
        return ResponseEntity.ok(reportes);
    }

    // Listar reportes por calendario preventivo
    @GetMapping("/calendario/{calendarioPreventivoId}")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<List<ReportePreventivoSalida>> listarPorCalendario(
            @PathVariable Integer calendarioPreventivoId) {
        return ResponseEntity.ok(reportePreventivoService.listarPorCalendario(calendarioPreventivoId));
    }
}
