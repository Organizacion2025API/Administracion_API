package org.esfe.ApiApexManagent.dtos.equipo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EquipoModificar {
    
    @NotNull(message = "El ID es requerido")
    private Integer id;
    
    @NotBlank(message = "El número de serie es requerido")
    private String nserie;
    
    @NotBlank(message = "El nombre del equipo es requerido")
    private String nombre;
    
    @NotBlank(message = "El modelo es requerido")
    private String modelo;
    
    @NotBlank(message = "La descripción es requerida")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;
    
    @NotNull(message = "La garantía es requerida")
    private Short garantia;
    
    private String img;
    
    @NotNull(message = "La categoría es requerida")
    private Integer categoriaId;
    
    @NotNull(message = "La ubicación es requerida")
    private Integer ubicacionId;

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
    
    public Integer getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Integer categoriaId) { this.categoriaId = categoriaId; }
    
    public Integer getUbicacionId() { return ubicacionId; }
    public void setUbicacionId(Integer ubicacionId) { this.ubicacionId = ubicacionId; }
}