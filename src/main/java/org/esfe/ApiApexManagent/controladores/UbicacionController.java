package org.esfe.ApiApexManagent.controladores;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionGuardar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionModificar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionSalida;
import org.esfe.ApiApexManagent.servicios.interfaces.IUbicacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

@RestController
@RequestMapping("/api/ubicaciones")
@Tag(name = "Ubicaciones", description = "API para la gestión de ubicaciones")
public class UbicacionController {

    @Autowired
    private IUbicacionService ubicacionService;

    @Operation(summary = "Obtener todas las ubicaciones paginadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicaciones encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))),
            @ApiResponse(responseCode = "204", description = "No hay ubicaciones para mostrar")
    })
    
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping
    public ResponseEntity<Page<UbicacionSalida>> listarUbicaciones(
            @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página", example = "10") @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Campo por el cual ordenar", example = "id") @RequestParam(defaultValue = "id") String sortBy,

            @Parameter(description = "Dirección de ordenamiento", example = "desc") @RequestParam(defaultValue = "desc") String sortDir) {

        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UbicacionSalida> ubicaciones = ubicacionService.listarUbicaciones(pageable);

        if (ubicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ubicaciones);
    }

    @Operation(summary = "Buscar ubicaciones por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicaciones encontradas"),
            @ApiResponse(responseCode = "204", description = "No se encontraron ubicaciones con ese nombre")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/buscar")
    public ResponseEntity<Page<UbicacionSalida>> buscarPorNombre(
            @Parameter(description = "Nombre de la ubicación a buscar") @RequestParam String nombre,

            @Parameter(description = "Número de página (0-based)", example = "0") @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página", example = "10") @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UbicacionSalida> ubicaciones = ubicacionService.buscarPorNombre(nombre, pageable);

        if (ubicaciones.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(ubicaciones);
    }

    @Operation(summary = "Obtener una ubicación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicación encontrada"),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada")
    })
    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<UbicacionSalida> obtenerPorId(
            @Parameter(description = "ID de la ubicación") @PathVariable Integer id) {

        Optional<UbicacionSalida> ubicacionOpt = ubicacionService.obtenerPorId(id);

        return ubicacionOpt.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva ubicación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ubicación creada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "409", description = "Ya existe una ubicación con ese nombre")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @PostMapping
    public ResponseEntity<UbicacionSalida> crearUbicacion(
            @Parameter(description = "Datos de la ubicación a crear") @Valid @RequestBody UbicacionGuardar ubicacionGuardar) {

        try {
            UbicacionSalida ubicacionCreada = ubicacionService.guardar(ubicacionGuardar);
            return ResponseEntity.status(HttpStatus.CREATED).body(ubicacionCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Actualizar una ubicación existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ubicación actualizada exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Ubicación no encontrada"),
            @ApiResponse(responseCode = "409", description = "Ya existe una ubicación con ese nombre")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @PutMapping("/{id}")
    public ResponseEntity<UbicacionSalida> actualizarUbicacion(
            @Parameter(description = "ID de la ubicación a actualizar") @PathVariable Integer id,

            @Parameter(description = "Datos actualizados de la ubicación") @Valid @RequestBody UbicacionModificar ubicacionModificar) {

        try {
            Optional<UbicacionSalida> ubicacionActualizada = ubicacionService.actualizar(id, ubicacionModificar);

            return ubicacionActualizada.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Verificar si existe una ubicación por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/existe-por-nombre")
    public ResponseEntity<Boolean> existePorNombre(
            @Parameter(description = "Nombre de la ubicación a verificar") @RequestParam String nombre) {

        boolean existe = ubicacionService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Verificar si existe una ubicación por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })

    @PreAuthorize("hasAuthority('ROLE_Administrador')")
    @GetMapping("/existe-por-id/{id}")
    public ResponseEntity<Boolean> existePorId(
            @Parameter(description = "ID de la ubicación a verificar") @PathVariable Integer id) {

        boolean existe = ubicacionService.existePorId(id);
        return ResponseEntity.ok(existe);
    }
}