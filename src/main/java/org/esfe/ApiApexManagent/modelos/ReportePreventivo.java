package org.esfe.ApiApexManagent.modelos;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class ReportePreventivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CalendarioPreventivoId", nullable = false) 
    private CalendarioPreventivo calendarioPreventivo;

    @JoinColumn(name = "PersonalId", nullable = false)
    private String personal;

    @NotBlank(message = "La observacion es requerida")
    @Size(max = 255, message = "La observación no puede exceder los 255 caracteres")
    @Column(name = "observacion", length = 255)
    private String observacion;

    @NotNull(message = "La fecha de atencion es requerida") 
    @Column(nullable = false)
    private LocalDateTime fechaAtencion;
    
    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    @NotNull(message = "El tipo de mantenimiento es requerido")
    private Short  tipoMantenimiento;

    @Column(nullable = false)
    private Short estado;

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

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public Short getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public CalendarioPreventivo getCalendarioPreventivo() {
        return calendarioPreventivo;
    }

    public void setCalendarioPreventivo(CalendarioPreventivo calendarioPreventivo) {
        this.calendarioPreventivo = calendarioPreventivo;
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

}
