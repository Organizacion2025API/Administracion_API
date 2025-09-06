package org.esfe.ApiApexManagent.dtos.ubicacion;

import jakarta.validation.constraints.NotBlank;

public class UbicacionGuardar {
    @NotBlank(message = "El nombre de la ubicación es requerido")
    private String nombreUbicacion;

    public UbicacionGuardar() {
    }

    public UbicacionGuardar(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }
}