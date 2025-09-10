package org.esfe.ApiApexManagent.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ubicacionId")
    private Ubicacion ubicacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaId")
    private Categoria categoria;

    @NotBlank(message = "El numero de serie es requerido")
    @Column(name = "n_serie", nullable = false, unique = true)
    private String nserie;

    @NotBlank(message = "El nombre del equipo es requerido")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El modelo es requerido")
    @Column(nullable = false)
    private String modelo;

    @NotBlank(message = "El descripcion es requerida")
    @Size(max = 255, message = "La descripción no puede tener más de 255 caracteres")
    private String descripcion;

    @Column(nullable = false)
    private short garantia;

    @Column(name = "img")
    private String img;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AsignacionEquipo> AsignacionEquipo = new HashSet<>();

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<CalendarioPreventivo> calendarioPreventivo = new HashSet<>();

    @Transient
    private boolean asignado;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    // Método para verificar si está asignado
    public boolean isAsignado() {
        return !this.AsignacionEquipo.isEmpty();
    }

    // Getter y Setter para el campo transitorio
    public boolean getAsignado() {
        return this.asignado;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }

    public String getNserie() {
        return nserie;
    }

    public void setNserie(String nserie) {
        this.nserie = nserie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public short getGarantia() {
        return garantia;
    }

    public void setGarantia(short garantia) {
        this.garantia = garantia;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
