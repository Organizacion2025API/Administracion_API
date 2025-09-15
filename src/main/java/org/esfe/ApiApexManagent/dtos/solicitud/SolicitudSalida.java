package org.esfe.ApiApexManagent.dtos.solicitud;

import java.time.LocalDateTime;

public class SolicitudSalida {
    private Integer id;
    private String descripcion;
    private short estado;
    private LocalDateTime fechaRegistro;
    private Integer asignacionEquipoId;
    private String personalId;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public short getEstado() { return estado; }
    public void setEstado(short estado) { this.estado = estado; }
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public Integer getAsignacionEquipoId() { return asignacionEquipoId; }
    public void setAsignacionEquipoId(Integer asignacionEquipoId) { this.asignacionEquipoId = asignacionEquipoId; }
    public String getPersonalId() { return personalId; }
    public void setPersonalId(String personalId) { this.personalId = personalId; }
}
