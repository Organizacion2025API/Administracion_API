package org.esfe.ApiApexManagent.modelos;

import java.time.LocalDateTime;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class ReporteCorrectivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @NotBlank(message = "La observacion es requerida")
    @Column(nullable = false, length = 1000)
    private String observacion;

    @NotNull(message = "La fecha de creacion es requerida")
    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El tipo de mantenimiento es requerido")
    @Column(nullable = false)
    private Short tipoMantenimiento; // Cambiado a Short

    @NotNull(message = "El estado es requerido")
    @Column(nullable = false)
    private Short estado; // Cambiado a Short

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitudId", nullable = false) // Corregido: "solicitudId" en minúscula
    private Solicitud solicitud;


    @JoinColumn(name = "personalId", nullable = false)
    private String personal;

    // Nuevo campo para el nombre del personal que realiza el reporte
    @Column(length = 100)
    private String nombrePersonal;

    // Constructor por defecto
    public ReporteCorrectivo() {
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Short getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public void setTipoMantenimiento(Short tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getNombrePersonal() {
        return nombrePersonal;
    }

    public void setNombrePersonal(String nombrePersonal) {
        this.nombrePersonal = nombrePersonal;
    }
}