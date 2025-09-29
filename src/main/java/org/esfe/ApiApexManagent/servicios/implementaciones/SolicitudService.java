package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.modelos.Solicitud;
import org.esfe.ApiApexManagent.repositorios.ISolicitudRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.ISolicitudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService implements ISolicitudService {

    @Autowired
    private ISolicitudRepository solicitudRepository;

    @Override
    @Transactional
    public Solicitud crearSolicitud(Solicitud solicitud) {
        solicitud.setFechaRegistro(LocalDateTime.now());
        solicitud.setEstado((short)1); // Estado "En espera" por defecto
        return solicitudRepository.save(solicitud);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Solicitud> obtenerPorId(Integer id) {
        return solicitudRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> listarPorPersonal(String personalId) {
        Integer id = Integer.valueOf(personalId);
        return solicitudRepository.findByAsignacionEquipo_PersonalId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> listarPorEstado(short estado) {
        return solicitudRepository.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Solicitud> listarTodas() {
        return solicitudRepository.findAll();
    }

    @Override
    @Transactional
    public boolean cambiarEstado(Integer id, short estado) {
        Optional<Solicitud> opt = solicitudRepository.findById(id);
        if (opt.isPresent()) {
            Solicitud solicitud = opt.get();
            solicitud.setEstado(estado);
            solicitudRepository.save(solicitud);
            return true;
        }
        return false;
    }
}
