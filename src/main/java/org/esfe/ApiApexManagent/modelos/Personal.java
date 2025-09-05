package org.esfe.ApiApexManagent.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Personal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RolId", nullable = false)
    private Rol rol;

    @NotBlank(message = "El nombre es requerido")
    @Column(nullable = false)
    private String nombre;

    @NotBlank(message = "El apellido es requerido")
    @Column(nullable = false)
    private String apellido;

    @NotBlank(message = "El telefono es requerido")
    @Pattern(regexp = "^\\d{4}-\\d{4}$", message = "El formato del teléfono debe ser 1234-5678")
    @Column(nullable = false)
    private String telefono;

    @Lob
    @Column(name = "img_personal", columnDefinition = "LONGBLOB")
    private byte[] imgPersonal;

    @NotBlank(message = "El email es requerido")
    @Email(message = "El formato del correo electrónico no es válido")
    @Column(nullable = false, unique = true)    
    private String email;

    @Column(nullable = false)
    private int status;

    @NotBlank(message = "El nombre de usuario es requerido")
    @Column(nullable = false, unique = true)
    private String username;


    @Column(nullable = false)
    @Size(min = 8, max = 255, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Solicitud> solicitud = new HashSet<>();

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ReporteCorrectivo> reporteCorrectivo = new HashSet<>();

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ReportePreventivo> reportePreventivo = new HashSet<>();

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<AsignacionEquipo> asignacionEquipos = new HashSet<>();

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

     public byte[] getImgPersonal() {
        return imgPersonal;
    }

    public void setImgPersonal(byte[] imgPersonal) {
        this.imgPersonal = imgPersonal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    // Método para mostrar imagen en Base64
    public String getImagenBase64() {
        if (this.imgPersonal != null && this.imgPersonal.length > 0) {
            return "data:image/jpeg;base64," + java.util.Base64.getEncoder().encodeToString(this.imgPersonal);
        }
        return "/img/default-equipo.jpg";
    }
}

