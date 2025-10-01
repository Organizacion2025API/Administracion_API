package org.esfe.ApiApexManagent.controladores;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/equipos")
@Tag(name = "Equipos", description = "API para la gestión de equipos")
public class EquipoController {

    @Autowired
    private IEquipoService equipoService;

   
     @GetMapping("/listar")
     @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<List<EquipoSalida>> listar() {
        List<EquipoSalida> equipos = equipoService.listarTodos();
        if (equipos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(equipos);
    }


    @Operation(summary = "Obtener todos los equipos paginados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipos encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay equipos para mostrar")
    })
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
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
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
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
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
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
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<EquipoSalida> crearEquipo(
            @Parameter(description = "Número de serie") @RequestParam("nserie") String nserie,
            @Parameter(description = "Nombre") @RequestParam("nombre") String nombre,
            @Parameter(description = "Modelo") @RequestParam("modelo") String modelo,
            @Parameter(description = "Descripción") @RequestParam("descripcion") String descripcion,
            @Parameter(description = "Garantía") @RequestParam("garantia") Short garantia,
            @Parameter(description = "ID categoría") @RequestParam("categoriaId") Integer categoriaId,
            @Parameter(description = "ID ubicación") @RequestParam("ubicacionId") Integer ubicacionId,
            @Parameter(description = "Archivo de imagen (opcional)") @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            // Crear objeto EquipoGuardar con los datos del formulario
            EquipoGuardar equipoGuardar = new EquipoGuardar();
            equipoGuardar.setNserie(nserie);
            equipoGuardar.setNombre(nombre);
            equipoGuardar.setModelo(modelo);
            equipoGuardar.setDescripcion(descripcion);
            equipoGuardar.setGarantia(garantia);
            equipoGuardar.setCategoriaId(categoriaId);
            equipoGuardar.setUbicacionId(ubicacionId);

            EquipoSalida equipoCreado = equipoService.guardar(equipoGuardar, imagen);
            return ResponseEntity.status(HttpStatus.CREATED).body(equipoCreado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Actualizar un equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "409", description = "Ya existe un equipo con ese número de serie")
    })
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<EquipoSalida> actualizarEquipo(
            @Parameter(description = "ID del equipo a actualizar") @PathVariable Integer id,
            @Parameter(description = "Nombre del equipo") @RequestParam("nombre") String nombre,
            @Parameter(description = "Número de serie del equipo") @RequestParam("nserie") String nserie,
            @Parameter(description = "Modelo del equipo") @RequestParam("modelo") String modelo,
            @Parameter(description = "Descripción del equipo") @RequestParam("descripcion") String descripcion,
            @Parameter(description = "Garantía del equipo") @RequestParam("garantia") Short garantia,
            @Parameter(description = "ID de la categoría") @RequestParam("categoriaId") Integer categoriaId,
            @Parameter(description = "ID de la ubicación") @RequestParam("ubicacionId") Integer ubicacionId,
            @Parameter(description = "Archivo de imagen del equipo (opcional)") @RequestParam(value = "imagen", required = false) MultipartFile imagen) {

        try {
            EquipoModificar equipoModificar = new EquipoModificar();
            equipoModificar.setNombre(nombre);
            equipoModificar.setNserie(nserie);
            equipoModificar.setModelo(modelo);
            equipoModificar.setDescripcion(descripcion);
            equipoModificar.setGarantia(garantia);
            equipoModificar.setCategoriaId(categoriaId);
            equipoModificar.setUbicacionId(ubicacionId);

            Optional<EquipoSalida> equipoActualizado = equipoService.actualizarConImagen(id, equipoModificar, imagen);
            return equipoActualizado.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "Eliminar un equipo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Equipo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Equipo no encontrado"),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque tiene asignaciones")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<String> eliminarEquipo(
            @Parameter(description = "ID del equipo a eliminar") @PathVariable Integer id) {

        try {
            boolean eliminado = equipoService.eliminar(id);
            if (eliminado) {
                return ResponseEntity.ok("Equipo eliminado exitosamente");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("No se puede eliminar el equipo porque tiene asignaciones activas");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al eliminar equipo: " + e.getMessage());
        }
    }

    @Operation(summary = "Verificar si existe un equipo por número de serie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    @GetMapping("/existe-por-nserie")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<Boolean> existePorNserie(
            @Parameter(description = "Número de serie a verificar") @RequestParam String nserie) {

        boolean existe = equipoService.existePorNserie(nserie);
        return ResponseEntity.ok(existe);
    }

    @Operation(summary = "Verificar si un equipo tiene asignaciones")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Verificación completada")
    })

    @GetMapping("/{id}/tiene-asignaciones")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<String> tieneAsignaciones(
            @Parameter(description = "ID del equipo a verificar") @PathVariable Integer id) {

        boolean tieneAsignaciones = equipoService.tieneAsignaciones(id);
        if (tieneAsignaciones) {
            return ResponseEntity.ok("El equipo tiene asignaciones");
        } else {
            return ResponseEntity.ok("El equipo no tiene asignaciones");
        }
    }
}