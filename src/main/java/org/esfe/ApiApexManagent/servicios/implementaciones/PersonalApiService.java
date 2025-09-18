package org.esfe.ApiApexManagent.servicios.implementaciones;

import org.esfe.ApiApexManagent.servicios.interfaces.IPersonalApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

@Service
public class PersonalApiService implements IPersonalApiService {

    @Value("${personal.api.url}")
    private String personalApiUrl;

    private final RestTemplate restTemplate;

    public PersonalApiService() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Optional<Integer> obtenerPersonalIdPorUsername(String username) {
        try {
            // Por ahora, usar una estrategia de mapeo simple basada en nombres conocidos
            // Esto evita tener que autenticarse contra la API externa
            System.out.println("[PERSONAL-API-DEBUG] Iniciando mapeo de username a personalId");
            System.out.println("[PERSONAL-API-DEBUG] Username recibido: " + username);
            
            // Mapeo temporal para usuarios conocidos
            Integer personalId = mapearUsernameAPersonalId(username);
            if (personalId != null) {
                System.out.println("[PERSONAL-API-DEBUG] PersonalId mapeado: " + personalId);
                return Optional.of(personalId);
            }
            
            // TODO: Implementar consulta real a la API cuando tengamos autenticación
            System.out.println("[PERSONAL-API-DEBUG] No se encontró mapeo para username: " + username);
            return Optional.empty();
            
        } catch (Exception e) {
            System.err.println("[PERSONAL-API-ERROR] Error inesperado: " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
    }
    
    /**
     * Mapeo temporal de usernames conocidos a personalIds
     * @param username Username del token JWT
     * @return PersonalId correspondiente o null si no se encuentra
     */
    private Integer mapearUsernameAPersonalId(String username) {
        // Mapeo basado en los usuarios conocidos del sistema
        switch (username.toLowerCase()) {
            case "admin":
                return 1; // ID del administrador
            case "tecnico":
            case "tecnico1":
                return 2; // ID del técnico
            case "empleado":
            case "empleado1":
                return 3; // ID del empleado
            default:
                // Intentar extraer ID numérico del username si es posible
                try {
                    return Integer.valueOf(username);
                } catch (NumberFormatException e) {
                    return null;
                }
        }
    }

    @Override
    public boolean existePersonalActivo(Integer personalId) {
        try {
            String url = personalApiUrl.trim();
            if (!url.endsWith("/")) {
                url += "/";
            }
            url += personalId;

            ResponseEntity<PersonalResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    createAuthHeaders(),
                    PersonalResponse.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                PersonalResponse personal = response.getBody();
                return personal.getStatus() == 1; // 1 = activo
            }
        } catch (RestClientException e) {
            System.err.println("[PERSONAL-API-ERROR] Error al verificar personal: " + e.getMessage());
        }

        return false;
    }

    @Override
    public boolean personalExiste(Integer personalId, String token) {
        try {
            // Por ahora, usar verificación simple basada en IDs conocidos
            System.out.println("[PERSONAL-API-DEBUG] Verificando si existe personalId: " + personalId);
            
            // Para los IDs mapeados, asumir que existen
            if (personalId != null && personalId > 0 && personalId <= 3) {
                System.out.println("[PERSONAL-API-DEBUG] PersonalId " + personalId + " existe (mapeo temporal)");
                return true;
            }
            
            System.out.println("[PERSONAL-API-DEBUG] PersonalId " + personalId + " no encontrado en mapeo");
            return false;
            
        } catch (Exception e) {
            System.err.println("[PERSONAL-API-ERROR] Error al verificar existencia de personal: " + e.getMessage());
            return false;
        }
    }

    private HttpEntity<String> createAuthHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        // Si la API externa requiere autenticación, agregar headers aquí
        return new HttpEntity<>(headers);
    }

    // DTO para mapear la respuesta de la API externa
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PersonalResponse {
        @JsonProperty("id")
        private Integer id;

        @JsonProperty("nombre")
        private String nombre;

        @JsonProperty("apellido")
        private String apellido;

        @JsonProperty("user")
        private String user;

        @JsonProperty("status")
        private Integer status;

        @JsonProperty("rolId")
        private Integer rolId;

        // Getters
        public Integer getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getUser() { return user; }
        public Integer getStatus() { return status; }
        public Integer getRolId() { return rolId; }

        // Setters
        public void setId(Integer id) { this.id = id; }
        public void setNombre(String nombre) { this.nombre = nombre; }
        public void setApellido(String apellido) { this.apellido = apellido; }
        public void setUser(String user) { this.user = user; }
        public void setStatus(Integer status) { this.status = status; }
        public void setRolId(Integer rolId) { this.rolId = rolId; }
    }
}