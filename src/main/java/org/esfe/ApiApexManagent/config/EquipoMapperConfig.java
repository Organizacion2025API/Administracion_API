package org.esfe.ApiApexManagent.config;

import org.esfe.ApiApexManagent.dtos.equipo.EquipoGuardar;
import org.esfe.ApiApexManagent.dtos.equipo.EquipoModificar;
import org.esfe.ApiApexManagent.modelos.Equipo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EquipoMapperConfig {

    @Bean
    public EquipoMapper equipoMapper() {
        return new EquipoMapper();
    }

    public class EquipoMapper {
        
        public Equipo toEntity(EquipoGuardar equipoGuardar) {
            if (equipoGuardar == null) {
                return null;
            }
            
            Equipo equipo = new Equipo();
            equipo.setNserie(equipoGuardar.getNserie());
            equipo.setNombre(equipoGuardar.getNombre());
            equipo.setModelo(equipoGuardar.getModelo());
            equipo.setDescripcion(equipoGuardar.getDescripcion());
            equipo.setGarantia(equipoGuardar.getGarantia());
            equipo.setImg(equipoGuardar.getImg());
            
            return equipo;
        }
        
        public Equipo toEntity(EquipoModificar equipoModificar, Equipo equipo) {
            if (equipoModificar == null || equipo == null) {
                return null;
            }
            
            equipo.setNserie(equipoModificar.getNserie());
            equipo.setNombre(equipoModificar.getNombre());
            equipo.setModelo(equipoModificar.getModelo());
            equipo.setDescripcion(equipoModificar.getDescripcion());
            equipo.setGarantia(equipoModificar.getGarantia());
            equipo.setImg(equipoModificar.getImg());
            
            return equipo;
        }
    }
}