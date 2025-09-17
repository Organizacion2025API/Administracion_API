package org.esfe.ApiApexManagent.controladores;

import org.esfe.ApiApexManagent.repositorios.IEquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Optional;

import org.esfe.ApiApexManagent.dtos.calendarioPreventivo.CalendarioPreventivoGuardar;
import org.esfe.ApiApexManagent.dtos.calendarioPreventivo.CalendarioPreventivoVer;
import org.esfe.ApiApexManagent.modelos.CalendarioPreventivo;
import org.esfe.ApiApexManagent.modelos.Equipo;
import org.esfe.ApiApexManagent.servicios.interfaces.ICalendarioPreventivoService;

@RestController
@RequestMapping("/api/calendario-preventivo")
@Tag(name = "Calendario Preventivo", description = "API para la gestión de mantenimientos preventivos")
public class CalendarioPreventivoController {

    @Autowired
    private ICalendarioPreventivoService calendarioService;

    @Autowired
    private IEquipoRepository equipoRepository;

    @Operation(summary = "Obtener todos los calendarios preventivos paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calendarios encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay calendarios para mostrar")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    @GetMapping
    public ResponseEntity<Page<CalendarioPreventivoVer>> listarCalendarios(
            @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de la página", example = "10") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo por el cual ordenar", example = "id") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección de ordenamiento", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CalendarioPreventivo> calendarios = calendarioService.listarTodos(pageable);
        if (calendarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Page<CalendarioPreventivoVer> dtoPage = calendarios.map(calendario -> {
            CalendarioPreventivoVer dto = new CalendarioPreventivoVer();
            dto.setId(calendario.getId());
            dto.setFechaInicio(calendario.getFechaInicio().toLocalDate());
            dto.setFechaFin(calendario.getFechaFin().toLocalDate());
            dto.setEstadoMantenimiento(calendario.getEstadoMantenimiento());
            if (calendario.getEquipo() != null) {
                dto.setEquipoId(calendario.getEquipo().getId());
                dto.setEquipoNombre(calendario.getEquipo().getNombre());
            }
            return dto;
        });
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(summary = "Obtener un calendario preventivo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calendario encontrado"),
            @ApiResponse(responseCode = "404", description = "Calendario no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    @GetMapping("/{id}")
    public ResponseEntity<CalendarioPreventivoVer> obtenerPorId(
            @Parameter(description = "ID del calendario") @PathVariable Integer id) {
        Optional<CalendarioPreventivo> calendarioOpt = calendarioService.obtenerPorId(id);
        if (calendarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        CalendarioPreventivo calendario = calendarioOpt.get();
        CalendarioPreventivoVer dto = new CalendarioPreventivoVer();
        dto.setId(calendario.getId());
        dto.setFechaInicio(calendario.getFechaInicio().toLocalDate());
        dto.setFechaFin(calendario.getFechaFin().toLocalDate());
        dto.setEstadoMantenimiento(calendario.getEstadoMantenimiento());
        if (calendario.getEquipo() != null) {
            dto.setEquipoId(calendario.getEquipo().getId());
            dto.setEquipoNombre(calendario.getEquipo().getNombre());
        }
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "Crear un nuevo calendario preventivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Calendario creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    @PostMapping
    public ResponseEntity<?> crearCalendarioPreventivo(
            @Parameter(description = "Datos del calendario a crear") @Valid @RequestBody CalendarioPreventivoGuardar dto) {

        Optional<Equipo> equipoOpt = equipoRepository.findById(dto.getEquipoId());
        if (equipoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Equipo no encontrado con ID: " + dto.getEquipoId());
        }

        // Validar fechas (1 mes exacto)
        if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
            return ResponseEntity.badRequest().body("Debe proporcionar fecha de inicio y fin");
        }
        LocalDateTime inicio = dto.getFechaInicio().atStartOfDay();
        LocalDateTime fin = dto.getFechaFin().atStartOfDay();
        long meses = java.time.temporal.ChronoUnit.MONTHS.between(inicio, fin);
        long dias = java.time.temporal.ChronoUnit.DAYS.between(inicio, fin);
        if (!(meses == 1 && (dias == 28 || dias == 29 || dias == 30 || dias == 31))) {
            return ResponseEntity.badRequest().body("El rango de fechas debe ser de 1 mes exacto");
        }

        // Mapear DTO a entidad y asociar equipo
        CalendarioPreventivo calendario = new CalendarioPreventivo();
        calendario.setFechaInicio(inicio);
        calendario.setFechaFin(fin);
        calendario.setEstadoMantenimiento(dto.getEstadoMantenimiento());
        calendario.setEquipo(equipoOpt.get());
        calendarioService.guardar(calendario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Calendario preventivo guardado exitosamente");
    }

}
