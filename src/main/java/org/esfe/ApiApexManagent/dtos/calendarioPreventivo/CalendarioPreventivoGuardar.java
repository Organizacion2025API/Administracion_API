package org.esfe.ApiApexManagent.dtos.calendarioPreventivo;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public class CalendarioPreventivoGuardar {
    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es requerida")
    private LocalDate fechaFin;

    @NotNull(message = "El estado de mantenimiento es requerido")
    private Short estadoMantenimiento;

    @NotNull(message = "El equipo es requerido")
    private Integer equipoId;

    // Getters y Setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public Short getEstadoMantenimiento() { return estadoMantenimiento; }
    public void setEstadoMantenimiento(Short estadoMantenimiento) { this.estadoMantenimiento = estadoMantenimiento; }
    public Integer getEquipoId() { return equipoId; }
    public void setEquipoId(Integer equipoId) { this.equipoId = equipoId; }
}
