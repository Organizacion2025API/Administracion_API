package org.esfe.ApiApexManagent.modelos;       

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class AsignacionEquipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;

    @Column(name = "personal_id", nullable = false)
    private Integer personalId;

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

    public AsignacionEquipo(Integer personalId, Equipo equipo) {
        this.personalId = personalId;
        this.equipo = equipo;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Integer getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Integer personalId) {
        this.personalId = personalId;
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
