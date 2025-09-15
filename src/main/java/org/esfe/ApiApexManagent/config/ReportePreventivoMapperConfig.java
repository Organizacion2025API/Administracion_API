package org.esfe.ApiApexManagent.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.esfe.ApiApexManagent.dtos.reportePreventivo.ReportePreventivoSalida;
import org.esfe.ApiApexManagent.modelos.ReportePreventivo;

@Mapper(componentModel = "spring")
public interface ReportePreventivoMapperConfig {
    ReportePreventivoMapperConfig INSTANCE = Mappers.getMapper(ReportePreventivoMapperConfig.class);

    @Mapping(source = "calendarioPreventivo.id", target = "calendarioPreventivoId")
    @Mapping(source = "personal.nombreCompleto", target = "nombrePersonal")
    @Mapping(source = "calendarioPreventivo.equipo.id", target = "equipoId")
    ReportePreventivoSalida toDto(ReportePreventivo entity);
}