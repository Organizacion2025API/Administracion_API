package org.esfe.ApiApexManagent.dtos.categoria;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CategoriaModificar {
    @NotNull(message = "El ID es requerido")
    private Integer id;
    
    @NotBlank(message = "El nombre de categoría es requerido")
    private String nombreCategoria;

    public CategoriaModificar() {
    }

    public CategoriaModificar(Integer id, String nombreCategoria) {
        this.id = id;
        this.nombreCategoria = nombreCategoria;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}