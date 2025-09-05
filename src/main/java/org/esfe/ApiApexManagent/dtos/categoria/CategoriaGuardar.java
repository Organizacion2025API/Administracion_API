package org.esfe.ApiApexManagent.dtos.categoria;

import jakarta.validation.constraints.NotBlank;

public class CategoriaGuardar {
    @NotBlank(message = "El nombre de categoría es requerido")
    private String nombreCategoria;

    public CategoriaGuardar() {
    }

    public CategoriaGuardar(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}