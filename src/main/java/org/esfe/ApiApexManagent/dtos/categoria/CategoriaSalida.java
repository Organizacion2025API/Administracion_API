package org.esfe.ApiApexManagent.dtos.categoria;

public class CategoriaSalida {
    private Integer id;
    private String nombreCategoria;

    public CategoriaSalida() {
    }

    public CategoriaSalida(Integer id, String nombreCategoria) {
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