package org.esfe.ApiApexManagent.dtos.reporteCorrectivo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReporteCorrectivoCrearRequest {
    @NotNull
    private Integer solicitudId;
    @NotNull
    @Size(min = 5, max = 500)
    private String observacion;
    @NotNull
    private Short tipoMantenimiento;
    // Getters y setters
    public Integer getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Integer solicitudId) { this.solicitudId = solicitudId; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Short getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(Short tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
}
