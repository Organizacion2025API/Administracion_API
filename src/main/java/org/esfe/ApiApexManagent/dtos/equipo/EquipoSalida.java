package org.esfe.ApiApexManagent.dtos.equipo;

import java.time.LocalDateTime;

public class EquipoSalida {
    
    private Integer id;
    private String nserie;
    private String nombre;
    private String modelo;
    private String descripcion;
    private Short garantia;
    private String img;
    private LocalDateTime fechaRegistro;
    private Integer categoriaId;
    private String categoriaNombre;
    private Integer ubicacionId;
    private String ubicacionNombre;
    private boolean asignado;

    public EquipoSalida() {}
    
    public EquipoSalida(Integer id, String nserie, String nombre, String modelo, 
                       String descripcion, Short garantia, String img, 
                       LocalDateTime fechaRegistro, Integer categoriaId, 
                       String categoriaNombre, Integer ubicacionId, 
                       String ubicacionNombre, boolean asignado) {
        this.id = id;
        this.nserie = nserie;
        this.nombre = nombre;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.garantia = garantia;
        this.img = img;
        this.fechaRegistro = fechaRegistro;
        this.categoriaId = categoriaId;
        this.categoriaNombre = categoriaNombre;
        this.ubicacionId = ubicacionId;
        this.ubicacionNombre = ubicacionNombre;
        this.asignado = asignado;
    }

    // Getters y Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    
    public String getNserie() { return nserie; }
    public void setNserie(String nserie) { this.nserie = nserie; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    
    public Short getGarantia() { return garantia; }
    public void setGarantia(Short garantia) { this.garantia = garantia; }
    
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
    
    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    
    public String getCategoriaNombre() { return categoriaNombre; }
    public void setCategoriaNombre(String categoriaNombre) { this.categoriaNombre = categoriaNombre; }
    
    public Integer getUbicacionId() { return ubicacionId; }
    public void setUbicacionId(Integer ubicacionId) { this.ubicacionId = ubicacionId; }
    
    public String getUbicacionNombre() { return ubicacionNombre; }
    public void setUbicacionNombre(String ubicacionNombre) { this.ubicacionNombre = ubicacionNombre; }
    
    public boolean isAsignado() { return asignado; }
    public void setAsignado(boolean asignado) { this.asignado = asignado; }
}