package org.esfe.ApiApexManagent.dtos.solicitud;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class SolicitudCrearRequest {
    @NotBlank
    private String descripcion;
    @NotNull
    private Integer asignacionEquipoId;

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getAsignacionEquipoId() { return asignacionEquipoId; }
    public void setAsignacionEquipoId(Integer asignacionEquipoId) { this.asignacionEquipoId = asignacionEquipoId; }
}
