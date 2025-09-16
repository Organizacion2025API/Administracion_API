package org.esfe.ApiApexManagent.controladores;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoGuardar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoModificar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoSalida;
import org.esfe.ApiApexManagent.servicios.interfaces.IEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/equipos")
@Tag(name = "Equipos", description = "API para la gestión de equipos")
public class EquipoController {

    @Autowired
    private IEquipoService equipoService;

    @Operation(summary = "Obtener todos los equipos paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay equipos para mostrar")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping
    public ResponseEntity<Page<EquipoSalida>> listarEquipos(
            @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página", example = "10") @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo por el cual ordenar", example = "id") @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Dirección de ordenamiento", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<EquipoSalida> equipos = equipoService.listarEquipos(pageable);

        if (equipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(equipos);
    }

    @Operation(summary = "Buscar equipos por número de serie, nombre o modelo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos encontrados"),
            @ApiResponse(responseCode = "204", description = "No se encontraron equipos")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/buscar")
    public ResponseEntity<Page<EquipoSalida>> buscarEquipos(
            @Parameter(description = "Número de serie a buscar") @RequestParam(required = false) String nserie,

            @Parameter(description = "Nombre a buscar") @RequestParam(required = false) String nombre,

            @Parameter(description = "Modelo a buscar") @RequestParam(required = false) String modelo,

            @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página", example = "10") @RequestParam(defaultValue = "10") int size) {

        // Si no se proporcionan parámetros de búsqueda, usar strings vacíos
        String nserieSearch = nserie != null ? nserie : "";
        String nombreSearch = nombre != null ? nombre : "";
        String modeloSearch = modelo != null ? modelo : "";

        Pageable pageable = PageRequest.of(page, size);
        Page<EquipoSalida> equipos = equipoService.buscarEquipos(nserieSearch, nombreSearch, modeloSearch, pageable);

        if (equipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(equipos);
    }

    @Operation(summary = "Obtener un equipo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo encontrado"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<EquipoSalida> obtenerPorId(
            @Parameter(description = "ID del equipo") @PathVariable Integer id) {

        Optional<EquipoSalida> equipoOpt = equipoService.obtenerPorId(id);
        return equipoOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Equipo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe un equipo con ese número de serie")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @PostMapping
    public ResponseEntity<EquipoSalida> crearEquipo(
            @Parameter(description = "Datos del equipo a crear") @Valid @RequestBody EquipoGuardar equipoGuardar) {

        try {
            EquipoSalida equipoCreado = equipoService.guardar(equipoGuardar);
            return ResponseEntity.status(HttpStatus.CREATED).body(equipoCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @Operation(summary = "Actualizar un equipo existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un equipo con ese número de serie")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @PutMapping("/{id}")
    public ResponseEntity<EquipoSalida> actualizarEquipo(
            @Parameter(description = "ID del equipo a actualizar") @PathVariable Integer id,

            @Parameter(description = "Datos actualizados del equipo") @Valid @RequestBody EquipoModificar equipoModificar) {

        try {
            Optional<EquipoSalida> equipoActualizado = equipoService.actualizar(id, equipoModificar);
            return equipoActualizado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        }
    }

    @Operation(summary = "Eliminar un equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque tiene asignaciones")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEquipo(
            @Parameter(description = "ID del equipo a eliminar") @PathVariable Integer id) {

        try {
            boolean eliminado = equipoService.eliminar(id);
            return eliminado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Verificar si existe un equipo por número de serie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/existe-por-nserie")
    public ResponseEntity<Boolean> existePorNserie(
            @Parameter(description = "Número de serie a verificar") @RequestParam String nserie) {

        boolean existe = equipoService.existePorNserie(nserie);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Verificar si un equipo tiene asignaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/{id}/tiene-asignaciones")
    public ResponseEntity<Boolean> tieneAsignaciones(
            @Parameter(description = "ID del equipo a verificar") @PathVariable Integer id) {

        boolean tieneAsignaciones = equipoService.tieneAsignaciones(id);
        return ResponseEntity.ok(tieneAsignaciones);
    }
}