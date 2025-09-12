package org.esfe.ApiApexManagent.servicios.interfaces;

import org.esfe.ApiApexManagent.modelos.CalendarioPreventivo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ICalendarioPreventivoService {
    CalendarioPreventivo guardar(CalendarioPreventivo calendario);
    Optional<CalendarioPreventivo> obtenerPorId(Integer id);
    Page<CalendarioPreventivo> listarTodos(Pageable pageable);
    Page<CalendarioPreventivo> listarPorEstado(Short estado, Pageable pageable);
    Page<CalendarioPreventivo> buscarCalendarios(String search, Short estado, Pageable pageable);
    void eliminar(Integer id);
}
