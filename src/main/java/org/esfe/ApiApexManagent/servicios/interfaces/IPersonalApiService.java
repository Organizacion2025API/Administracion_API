package org.esfe.ApiApexManagent.servicios.interfaces;

import java.util.Optional;

public interface IPersonalApiService {
    /**
     * Obtiene el ID del personal basado en el nombre de usuario
     * @param username Nombre de usuario
     * @return Optional con el ID del personal si se encuentra
     */
    Optional<Integer> obtenerPersonalIdPorUsername(String username);
    
    /**
     * Verifica si el personal existe y está activo
     * @param personalId ID del personal
     * @return true si existe y está activo
     */
    boolean existePersonalActivo(Integer personalId);
    
    /**
     * Verifica si el personal existe
     * @param personalId ID del personal
     * @param token Token de autenticación
     * @return true si existe
     */
    boolean personalExiste(Integer personalId, String token);
}