package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.modelos.Solicitud;
import java.util.List;
import java.util.Optional;

public interface ISolicitudService {
    Solicitud crearSolicitud(Solicitud solicitud);
    Optional<Solicitud> obtenerPorId(Integer id);
    List<Solicitud> listarPorPersonal(String personalId);
    List<Solicitud> listarPorEstado(short estado);
    List<Solicitud> listarTodas();
    boolean cambiarEstado(Integer id, short estado);
}
