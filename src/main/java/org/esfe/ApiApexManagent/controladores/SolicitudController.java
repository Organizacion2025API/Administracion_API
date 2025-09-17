package org.esfe.ApiApexManagent.controladores;

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
public class SolicitudController {

    @Autowired
    private ISolicitudService solicitudService;
    @Autowired
    private AsignacionEquipoRepository asignacionEquipoRepository;

    // Crear solicitud de mantenimiento correctivo (cualquier usuario autenticado)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> crearSolicitud(@Valid @RequestBody SolicitudCrearRequest request,
            Authentication authentication, HttpServletRequest httpRequest) {
        // Extraer el rolid del JWT claims
        Object claimsObj = httpRequest.getAttribute("claims");
        Integer personalId;
        if (claimsObj != null && claimsObj instanceof io.jsonwebtoken.Claims) {
            Object rolidObj = ((io.jsonwebtoken.Claims) claimsObj).get("rolid");
            personalId = Integer.valueOf(rolidObj.toString());
        } else {
            // Fallback: usa el nombre de usuario si no hay claims
            personalId = Integer.valueOf(authentication.getName());
        }
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
        Solicitud solicitud = new Solicitud();
        solicitud.setDescripcion(request.getDescripcion());
        solicitud.setAsignacionEquipo(asignacion);
        solicitud.setPersonal(personalId.toString());
        Solicitud guardada = solicitudService.crearSolicitud(solicitud);
        return ResponseEntity.status(HttpStatus.CREATED).body(mapToSalida(guardada));
    }

    // Listar solicitudes del usuario autenticado
    @GetMapping("/mias")
    public ResponseEntity<List<SolicitudSalida>> listarMisSolicitudes(Authentication authentication,
            HttpServletRequest httpRequest) {
        Object claimsObj = httpRequest.getAttribute("claims");
        Integer personalId;
        if (claimsObj != null && claimsObj instanceof io.jsonwebtoken.Claims) {
            Object rolidObj = ((io.jsonwebtoken.Claims) claimsObj).get("rolid");
            personalId = Integer.valueOf(rolidObj.toString());
        } else {
            personalId = Integer.valueOf(authentication.getName());
        }
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
