package org.esfe.ApiApexManagent.dtos.ubicacion;

public class UbicacionSalida {
    private Integer id;
    private String nombreUbicacion;

    public UbicacionSalida() {
    }

    public UbicacionSalida(Integer id, String nombreUbicacion) {
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