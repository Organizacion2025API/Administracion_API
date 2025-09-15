package org.esfe.ApiApexManagent.dtos.reporteCorrectivo;

import java.time.LocalDateTime;

public class ReporteCorrectivoSalida {
    private Integer id;
    private Integer solicitudId;
    private String observacion;
    private Short tipoMantenimiento;
    private Short estado;
    private LocalDateTime fechaCreacion;
    private String nombrePersonal;
    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getSolicitudId() { return solicitudId; }
    public void setSolicitudId(Integer solicitudId) { this.solicitudId = solicitudId; }
    public String getObservacion() { return observacion; }
    public void setObservacion(String observacion) { this.observacion = observacion; }
    public Short getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(Short tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
    public Short getEstado() { return estado; }
    public void setEstado(Short estado) { this.estado = estado; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getNombrePersonal() { return nombrePersonal; }
    public void setNombrePersonal(String nombrePersonal) { this.nombrePersonal = nombrePersonal; }
}
