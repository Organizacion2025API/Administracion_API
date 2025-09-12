package org.esfe.ApiApexManagent.controladores;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaGuardar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaModificar;
import org.esfe.ApiApexManagent.dtos.categoria.CategoriaSalida;
import org.esfe.ApiApexManagent.servicios.interfaces.ICategoriaService;
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
@RequestMapping("/api/categorias")
@Tag(name = "Categorías", description = "API para la gestión de categorías")
public class CategoriaController {

    @Autowired
    private ICategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías paginadas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías encontradas",
                     content = @Content(mediaType = "application/json", 
                     schema = @Schema(implementation = Page.class))),
        @ApiResponse(responseCode = "204", description = "No hay categorías para mostrar")
    })
    @PreAuthorize("hasRole('Administrador')")
    @GetMapping
    public ResponseEntity<Page<CategoriaSalida>> listarCategorias(
            @Parameter(description = "Número de página (0-based)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página", example = "10") 
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo por el cual ordenar", example = "id") 
            @RequestParam(defaultValue = "id") String sortBy,
            
            @Parameter(description = "Dirección de ordenamiento", example = "desc") 
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<CategoriaSalida> categorias = categoriaService.listarCategorias(pageable);
        
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar categorías por nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categorías encontradas"),
        @ApiResponse(responseCode = "204", description = "No se encontraron categorías con ese nombre")
    })
    @PreAuthorize("hasRole('Administrador')")
    @GetMapping("/buscar")
    public ResponseEntity<Page<CategoriaSalida>> buscarPorNombre(
            @Parameter(description = "Nombre de la categoría a buscar") 
            @RequestParam String nombre,
            
            @Parameter(description = "Número de página (0-based)", example = "0") 
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de la página", example = "10") 
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<CategoriaSalida> categorias = categoriaService.buscarPorNombre(nombre, pageable);
        
        if (categorias.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Obtener una categoría por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PreAuthorize("hasRole('Administrador')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaSalida> obtenerPorId(
            @Parameter(description = "ID de la categoría") 
            @PathVariable Integer id) {
        
        Optional<CategoriaSalida> categoriaOpt = categoriaService.obtenerPorId(id);
        
        return categoriaOpt.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear una nueva categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre")
    })
    @PostMapping
    public ResponseEntity<CategoriaSalida> crearCategoria(
            @Parameter(description = "Datos de la categoría a crear") 
            @Valid @RequestBody CategoriaGuardar categoriaGuardar) {
        
        try {
            CategoriaSalida categoriaCreada = categoriaService.guardar(categoriaGuardar);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoriaCreada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Actualizar una categoría existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada"),
        @ApiResponse(responseCode = "409", description = "Ya existe una categoría con ese nombre")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaSalida> actualizarCategoria(
            @Parameter(description = "ID de la categoría a actualizar") 
            @PathVariable Integer id,
            
            @Parameter(description = "Datos actualizados de la categoría") 
            @Valid @RequestBody CategoriaModificar categoriaModificar) {
        
        try {
            Optional<CategoriaSalida> categoriaActualizada = categoriaService.actualizar(id, categoriaModificar);
            
            return categoriaActualizada.map(ResponseEntity::ok)
                                      .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Eliminar una categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(
            @Parameter(description = "ID de la categoría a eliminar") 
            @PathVariable Integer id) {
        
        if (categoriaService.eliminar(id)) {
            return ResponseEntity.noContent().build();
        }
        
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Verificar si existe una categoría por nombre")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Verificación completada")
    })
    @GetMapping("/existe-por-nombre")
    public ResponseEntity<Boolean> existePorNombre(
            @Parameter(description = "Nombre de la categoría a verificar") 
            @RequestParam String nombre) {
        
        boolean existe = categoriaService.existePorNombre(nombre);
        return ResponseEntity.ok(existe);
    }
}