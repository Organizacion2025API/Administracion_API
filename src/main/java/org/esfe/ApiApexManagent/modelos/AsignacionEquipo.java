package org.esfe.ApiApexManagent.modelos;       

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AsignacionEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @JoinColumn(name = "personalId", nullable = false)
    private String personal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipoId", nullable = false)
    private Equipo equipo;

    @OneToMany(mappedBy = "asignacionEquipo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Solicitud> solicitud = new HashSet<>();

    public AsignacionEquipo() {
    // inicialización si es necesaria
        }

    public Integer getId() {
        return Id;
    }

    public AsignacionEquipo(String personal, Equipo equipo) {
        this.personal = personal;
        this.equipo = equipo;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public Set<Solicitud> getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Set<Solicitud> solicitud) {
        this.solicitud = solicitud;
    }


}
