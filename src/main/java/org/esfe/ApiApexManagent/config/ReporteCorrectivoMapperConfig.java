package org.esfe.ApiApexManagent.config;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.esfe.ApiApexManagent.dtos.reporteCorrectivo.ReporteCorrectivoSalida;
import org.esfe.ApiApexManagent.modelos.ReporteCorrectivo;

@Mapper(componentModel = "spring")
public interface ReporteCorrectivoMapperConfig {
    ReporteCorrectivoMapperConfig INSTANCE = Mappers.getMapper(ReporteCorrectivoMapperConfig.class);

    @Mapping(source = "solicitud.id", target = "solicitudId")
    ReporteCorrectivoSalida toDto(ReporteCorrectivo entity);
}
