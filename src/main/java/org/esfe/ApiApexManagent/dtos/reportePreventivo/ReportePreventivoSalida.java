package org.esfe.ApiApexManagent.dtos.reportePreventivo;

import java.time.LocalDateTime;

public class ReportePreventivoSalida {
    private Integer id;
    private Integer calendarioPreventivoId;
    private String observacion;
    private Short tipoMantenimiento;
    private Short estado;
    private LocalDateTime fechaAtencion;
    private String nombrePersonal;
    private Integer equipoId;
    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getCalendarioPreventivoId() { return calendarioPreventivoId; }
    public void setCalendarioPreventivoId(Integer calendarioPreventivoId) { this.calendarioPreventivoId = calendarioPreventivoId; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Short getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(Short tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
    public Short getEstado() { return estado; }
    public void setEstado(Short estado) { this.estado = estado; }
    public LocalDateTime getFechaAtencion() { return fechaAtencion; }
    public void setFechaAtencion(LocalDateTime fechaAtencion) { this.fechaAtencion = fechaAtencion; }
    public String getNombrePersonal() { return nombrePersonal; }
    public void setNombrePersonal(String nombrePersonal) { this.nombrePersonal = nombrePersonal; }
    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }
}
