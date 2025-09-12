package org.esfe.ApiApexManagent.dtos.calendarioPreventivo;

import java.time.LocalDate;

public class CalendarioPreventivoVer {
    private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Short estadoMantenimiento;
    private Integer equipoId;
    private String equipoNombre;

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Short getEstadoMantenimiento() { return estadoMantenimiento; }
    public void setEstadoMantenimiento(Short estadoMantenimiento) { this.estadoMantenimiento = estadoMantenimiento; }
    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }
    public String getEquipoNombre() { return equipoNombre; }
    public void setEquipoNombre(String equipoNombre) { this.equipoNombre = equipoNombre; }
}
