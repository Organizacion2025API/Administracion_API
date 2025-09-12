package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.modelos.CalendarioPreventivo;
import org.esfe.ApiApexManagent.repositorios.ICalendarioPreventivoRepository;
import org.esfe.ApiApexManagent.servicios.interfaces.ICalendarioPreventivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarioPreventivoService implements ICalendarioPreventivoService {

    @Autowired
    private ICalendarioPreventivoRepository calendarioRepo;

    @Override
    public CalendarioPreventivo guardar(CalendarioPreventivo calendario) {
        return calendarioRepo.save(calendario);
    }

    @Override
    public Optional<CalendarioPreventivo> obtenerPorId(Integer id) {
        return calendarioRepo.findById(id);
    }

    @Override
    public Page<CalendarioPreventivo> listarTodos(Pageable pageable) {
        return calendarioRepo.findAll(pageable);
    }

    @Override
    public Page<CalendarioPreventivo> listarPorEstado(Short estado, Pageable pageable) {
        return calendarioRepo.findByEstadoMantenimiento(estado, pageable);
    }

    @Override
    public Page<CalendarioPreventivo> buscarCalendarios(String search, Short estado, Pageable pageable) {
        if (search != null && !search.isEmpty() && estado != null) {
            return calendarioRepo.findByEquipo_NombreContainingIgnoreCaseAndEstadoMantenimiento(search, estado, pageable);
        } else if (search != null && !search.isEmpty()) {
            return calendarioRepo.findByEquipo_NombreContainingIgnoreCase(search, pageable);
        } else if (estado != null) {
            return calendarioRepo.findByEstadoMantenimiento(estado, pageable);
        } else {
            return calendarioRepo.findAll(pageable);
        }
    }

    @Override
    public void eliminar(Integer id) {
        calendarioRepo.deleteById(id);
    }
}
