package org.esfe.ApiApexManagent.controladores;

import org.esfe.ApiApexManagent.controladores.base.BaseController;
import org.esfe.ApiApexManagent.dtos.solicitud.SolicitudCrearRequest;
import org.esfe.ApiApexManagent.dtos.solicitud.SolicitudSalida;
import org.esfe.ApiApexManagent.modelos.AsignacionEquipo;
import org.esfe.ApiApexManagent.modelos.Solicitud;
import org.esfe.ApiApexManagent.repositorios.AsignacionEquipoRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.ISolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController extends BaseController {

    @Autowired
    private ISolicitudService solicitudService;
    @Autowired
    private AsignacionEquipoRepository asignacionEquipoRepository;

    // Crear solicitud de mantenimiento correctivo (cualquier usuario autenticado)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> crearSolicitud(@Valid @RequestBody SolicitudCrearRequest request,
            Authentication authentication, HttpServletRequest httpRequest) {
        
        System.out.println("[SOLICITUD-DEBUG] Iniciando creación de solicitud");
        
        // Extraer personalId usando el método utilitario
        Optional<Integer> personalIdOpt = extraerPersonalId(authentication, httpRequest);
        if (personalIdOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("No se pudo determinar el ID del usuario desde el token JWT");
        }
        
        Integer personalId = personalIdOpt.get();
        System.out.println("[SOLICITUD-DEBUG] PersonalId final: " + personalId);
        
        Optional<AsignacionEquipo> asignacionOpt = asignacionEquipoRepository.findById(request.getAsignacionEquipoId());
        if (asignacionOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asignación de equipo no encontrada");
        }
        
        AsignacionEquipo asignacion = asignacionOpt.get();
        
        // Validar que el equipo esté asignado al usuario autenticado
        if (!asignacion.getPersonalId().equals(personalId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("No puedes crear solicitud para un equipo no asignado a ti");
        }
        
        // Crear la solicitud
        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setAsignacionEquipo(asignacion);
        solicitud.setPersonal(personalId.toString());
        solicitud.setEstado((short) 1); // 1 = Pendiente
        solicitud.setFechaRegistro(java.time.LocalDateTime.now());
        
        try {
            Solicitud guardada = solicitudService.crearSolicitud(solicitud);
            return ResponseEntity.status(HttpStatus.CREATED).body(mapToSalida(guardada));
        } catch (Exception e) {
            System.err.println("[ERROR] Error al crear solicitud: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al crear la solicitud");
        }
    }

    // Listar solicitudes del usuario autenticado
    @GetMapping("/mias")
    public ResponseEntity<List<SolicitudSalida>> listarMisSolicitudes(Authentication authentication,
            HttpServletRequest httpRequest) {
        
        // Extraer personalId usando el método utilitario
        Optional<Integer> personalIdOpt = extraerPersonalId(authentication, httpRequest);
        if (personalIdOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Integer personalId = personalIdOpt.get();
        List<Solicitud> solicitudes = solicitudService.listarPorPersonal(personalId.toString());
        List<SolicitudSalida> salida = solicitudes.stream().map(this::mapToSalida).collect(Collectors.toList());
        return ResponseEntity.ok(salida);
    }

    // Cambiar estado de solicitud (solo admin)
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam short estado) {
        boolean ok = solicitudService.cambiarEstado(id, estado);
        if (ok)
            return ResponseEntity.ok().build();
        return ResponseEntity.notFound().build();
    }

    private SolicitudSalida mapToSalida(Solicitud s) {
        SolicitudSalida dto = new SolicitudSalida();
        dto.setId(s.getId());
        dto.setDescripcion(s.getDescripcion());
        dto.setEstado(s.getEstado());
        dto.setFechaRegistro(s.getFechaRegistro());
        dto.setAsignacionEquipoId(s.getAsignacionEquipo().getId());
        dto.setPersonalId(s.getPersonal());
        return dto;
    }
}
