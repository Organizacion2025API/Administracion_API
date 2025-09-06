package org.esfe.ApiApexManagent.dtos.ubicacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UbicacionModificar {
    @NotNull(message = "El ID es requerido")
    private Integer id;
    
    @NotBlank(message = "El nombre de la ubicación es requerido")
    private String nombreUbicacion;

    public UbicacionModificar() {
    }

    public UbicacionModificar(Integer id, String nombreUbicacion) {
        this.id = id;
        this.nombreUbicacion = nombreUbicacion;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }
}