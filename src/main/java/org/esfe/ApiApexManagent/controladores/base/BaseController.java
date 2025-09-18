package org.esfe.ApiApexManagent.controladores.base;

import org.esfe.ApiApexManagent.servicios.interfaces.IPersonalApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public abstract class BaseController {

    @Autowired
    protected IPersonalApiService personalApiService;

    /**
     * Extrae el personalId del token JWT o mediante consulta a la API externa
     * @param authentication Objeto de autenticación
     * @param httpRequest Request HTTP para acceder a los claims
     * @return Optional con el personalId si se encuentra
     */
    protected Optional<Integer> extraerPersonalId(Authentication authentication, HttpServletRequest httpRequest) {
        System.out.println("[BASE-CONTROLLER-DEBUG] Iniciando extracción de personalId");
        
        // Primero intentar extraer desde los claims del JWT
        Object claimsObj = httpRequest.getAttribute("claims");
        
        if (claimsObj != null && claimsObj instanceof io.jsonwebtoken.Claims) {
            io.jsonwebtoken.Claims claims = (io.jsonwebtoken.Claims) claimsObj;
            
            System.out.println("[BASE-CONTROLLER-DEBUG] Claims disponibles: " + claims.keySet());
            
            // Intentar extraer rolid, userId, user_id, o sub
            String[] claimKeys = {"rolid", "userId", "user_id", "sub", "id"};
            for (String key : claimKeys) {
                Object idObj = claims.get(key);
                if (idObj != null) {
                    try {
                        Integer personalId = Integer.valueOf(idObj.toString());
                        System.out.println("[BASE-CONTROLLER-DEBUG] PersonalId extraído de claim '" + key + "': " + personalId);
                        return Optional.of(personalId);
                    } catch (NumberFormatException e) {
                        System.err.println("[BASE-CONTROLLER-ERROR] No se pudo convertir " + key + " a entero: " + idObj);
                    }
                }
            }
        }
        
        // Si no se puede extraer desde claims, usar el username para consultar la API
        String username = authentication.getName();
        System.out.println("[BASE-CONTROLLER-DEBUG] Consultando API externa para username: " + username);
        
        Optional<Integer> personalIdOpt = personalApiService.obtenerPersonalIdPorUsername(username);
        if (personalIdOpt.isPresent()) {
            System.out.println("[BASE-CONTROLLER-DEBUG] PersonalId obtenido de API externa: " + personalIdOpt.get());
            return personalIdOpt;
        }
        
        // Como último recurso, intentar convertir el username a número
        try {
            Integer personalId = Integer.valueOf(username);
            System.out.println("[BASE-CONTROLLER-DEBUG] PersonalId extraído del username: " + personalId);
            return Optional.of(personalId);
        } catch (NumberFormatException e) {
            System.err.println("[BASE-CONTROLLER-ERROR] No se pudo extraer personalId de ninguna fuente");
            return Optional.empty();
        }
    }
}