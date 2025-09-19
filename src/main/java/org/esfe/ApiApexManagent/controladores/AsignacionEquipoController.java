package org.esfe.ApiApexManagent.controladores;

import org.esfe.ApiApexManagent.dtos.asignacion.AsignacionEquipoRequest;
import org.esfe.ApiApexManagent.modelos.AsignacionEquipo;
import org.esfe.ApiApexManagent.modelos.Equipo;
import org.esfe.ApiApexManagent.repositorios.AsignacionEquipoRepository;
import org.esfe.ApiApexManagent.repositorios.IEquipoRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.IPersonalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import java.util.Optional;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionEquipoController {

    @Autowired
    private IEquipoRepository equipoRepository;
    @Autowired
    private AsignacionEquipoRepository asignacionEquipoRepository;
    @Autowired
    private IPersonalApiService personalApiService;

    @GetMapping
    public ResponseEntity<List<AsignacionEquipoDTO>> listarAsignaciones() {
        List<AsignacionEquipoDTO> asignaciones = asignacionEquipoRepository.findAll().stream()
                .map(a -> new AsignacionEquipoDTO(
                        a.getId(),
                        a.getPersonalId(),
                        a.getEquipo() != null ? a.getEquipo().getId() : null,
                        a.getEquipo() != null ? a.getEquipo().getNombre() : null,
                        a.getEquipo() != null ? a.getEquipo().getDescripcion() : null,
                        a.getEquipo() != null ? a.getEquipo().getModelo() : null,
                        a.getEquipo() != null ? a.getEquipo().getNserie() : null))
                .collect(Collectors.toList());
        return ResponseEntity.ok(asignaciones);
    }

    // DTO simple para la respuesta
    public static class AsignacionEquipoDTO {
        private Integer id;
        private Integer personalId;
        private Integer equipoId;
        private String equipoNombre;
        private String equipoDescripcion;
        private String equipoModelo;
        private String equipoNserie;

        public AsignacionEquipoDTO(Integer id, Integer personalId, Integer equipoId, String equipoNombre, 
                                  String equipoDescripcion, String equipoModelo, String equipoNserie) {
            this.id = id;
            this.personalId = personalId;
            this.equipoId = equipoId;
            this.equipoNombre = equipoNombre;
            this.equipoDescripcion = equipoDescripcion;
            this.equipoModelo = equipoModelo;
            this.equipoNserie = equipoNserie;
        }

        public Integer getId() {
            return id;
        }

        public Integer getPersonalId() {
            return personalId;
        }

        public Integer getEquipoId() {
            return equipoId;
        }

        public String getEquipoNombre() {
            return equipoNombre;
        }

        public String getEquipoDescripcion() {
            return equipoDescripcion;
        }

        public String getEquipoModelo() {
            return equipoModelo;
        }

        public String getEquipoNserie() {
            return equipoNserie;
        }
    }

    @PostMapping
    public ResponseEntity<?> asignarEquipo(@RequestBody AsignacionEquipoRequest request,
            @RequestHeader("Authorization") String token) {
        // 1. Verificar que el equipo existe en MySQL
        Optional<Equipo> equipoOpt = equipoRepository.findById(request.getEquipoId());
        if (equipoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Equipo no encontrado");
        }

        // 2. Verificar que el personal existe (sin importar el rol)
        // El método personalExiste ya valida que el personalId sea válido
        if (!personalApiService.personalExiste(request.getPersonalId(), token)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Personal no encontrado");
        }

        // 3. Verificar que el equipo no esté ya asignado a otro personal
        List<AsignacionEquipo> asignacionesExistentes = asignacionEquipoRepository.findByEquipo_Id(request.getEquipoId());
        if (!asignacionesExistentes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("El equipo ya está asignado a otro personal");
        }

        // 4. Registrar la asignación en MySQL usando el modelo con relaciones
        AsignacionEquipo asignacion = new AsignacionEquipo();
        asignacion.setPersonalId(request.getPersonalId());
        asignacion.setEquipo(equipoOpt.get());
        AsignacionEquipo asignacionGuardada = asignacionEquipoRepository.save(asignacion);

        // 5. Retornar respuesta con detalles completos del equipo asignado
        AsignacionEquipoDTO response = new AsignacionEquipoDTO(
                asignacionGuardada.getId(),
                asignacionGuardada.getPersonalId(),
                asignacionGuardada.getEquipo().getId(),
                asignacionGuardada.getEquipo().getNombre(),
                asignacionGuardada.getEquipo().getDescripcion(),
                asignacionGuardada.getEquipo().getModelo(),
                asignacionGuardada.getEquipo().getNserie()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_Tecnico')")
    public ResponseEntity<?> eliminarAsignacion(@PathVariable Integer id) {
        if (asignacionEquipoRepository.existsById(id)) {
            asignacionEquipoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
