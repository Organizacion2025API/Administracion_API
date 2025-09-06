package org.esfe.ApiApexManagent.config;

import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionGuardar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionModificar;
import org.esfe.ApiApexManagent.dtos.ubicacion.UbicacionSalida;
import org.esfe.ApiApexManagent.modelos.Ubicacion;
import org.springframework.stereotype.Component;

@Component
public class UbicacionMapperConfig {
    
    public UbicacionSalida toDto(Ubicacion ubicacion) {
        if (ubicacion == null) {
            return null;
        }
        
        return new UbicacionSalida(
            ubicacion.getId(),
            ubicacion.getNombreUbicacion()
        );
    }
    
    public Ubicacion toEntity(UbicacionGuardar ubicacionGuardar) {
        if (ubicacionGuardar == null) {
            return null;
        }
        
        Ubicacion ubicacion = new Ubicacion();
        ubicacion.setNombreUbicacion(ubicacionGuardar.getNombreUbicacion());
        return ubicacion;
    }
    
    public Ubicacion toEntity(UbicacionModificar ubicacionModificar, Ubicacion ubicacion) {
        if (ubicacionModificar == null || ubicacion == null) {
            return null;
        }
        
        ubicacion.setNombreUbicacion(ubicacionModificar.getNombreUbicacion());
        return ubicacion;
    }
}