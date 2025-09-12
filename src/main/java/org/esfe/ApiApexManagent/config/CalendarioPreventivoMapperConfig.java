package org.esfe.ApiApexManagent.config;

import org.esfe.ApiApexManagent.dtos.calendarioPreventivo.CalendarioPreventivoGuardar;
import org.esfe.ApiApexManagent.dtos.calendarioPreventivo.CalendarioPreventivoModificar;
import org.esfe.ApiApexManagent.modelos.CalendarioPreventivo;
import org.esfe.ApiApexManagent.modelos.Equipo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CalendarioPreventivoMapperConfig {
    @Bean
    public CalendarioPreventivoMapper calendarioPreventivoMapper() {
        return new CalendarioPreventivoMapper();
    }

    public class CalendarioPreventivoMapper {
        public CalendarioPreventivo toEntity(CalendarioPreventivoGuardar dto, Equipo equipo) {
            if (dto == null || equipo == null) return null;
            CalendarioPreventivo c = new CalendarioPreventivo();
            if (dto.getFechaInicio() != null)
                c.setFechaInicio(dto.getFechaInicio().atStartOfDay());
            if (dto.getFechaFin() != null)
                c.setFechaFin(dto.getFechaFin().atStartOfDay());
            c.setEstadoMantenimiento(dto.getEstadoMantenimiento());
            c.setEquipo(equipo);
            return c;
        }
        public CalendarioPreventivo toEntity(CalendarioPreventivoModificar dto, CalendarioPreventivo c, Equipo equipo) {
            if (dto == null || c == null || equipo == null) return null;
            if (dto.getFechaInicio() != null)
                c.setFechaInicio(dto.getFechaInicio().atStartOfDay());
            if (dto.getFechaFin() != null)
                c.setFechaFin(dto.getFechaFin().atStartOfDay());
            c.setEstadoMantenimiento(dto.getEstadoMantenimiento());
            c.setEquipo(equipo);
            return c;
        }
    }
}
