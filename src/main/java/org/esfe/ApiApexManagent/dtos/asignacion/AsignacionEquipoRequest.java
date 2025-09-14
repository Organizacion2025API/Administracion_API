package org.esfe.ApiApexManagent.dtos.asignacion;

public class AsignacionEquipoRequest {
    private Integer equipoId;
    private Integer personalId;

    public Integer getEquipoId() {
        return equipoId;
    }
    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }
    public Integer getPersonalId() {
        return personalId;
    }
    public void setPersonalId(Integer personalId) {
        this.personalId = personalId;
    }
}