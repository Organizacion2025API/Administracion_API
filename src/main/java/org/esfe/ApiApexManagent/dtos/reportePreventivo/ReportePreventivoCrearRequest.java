package org.esfe.ApiApexManagent.dtos.reportePreventivo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReportePreventivoCrearRequest {
    @NotNull
    private Integer calendarioPreventivoId;
    @NotNull
    @Size(min = 5, max = 500)
    private String observacion;
    @NotNull
    private Short tipoMantenimiento;
    // Getters y setters
    public Integer getCalendarioPreventivoId() { return calendarioPreventivoId; }
    public void setCalendarioPreventivoId(Integer calendarioPreventivoId) { this.calendarioPreventivoId = calendarioPreventivoId; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Short getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(Short tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
}
