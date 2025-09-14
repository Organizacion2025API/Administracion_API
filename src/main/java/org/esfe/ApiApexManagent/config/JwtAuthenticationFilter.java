package org.esfe.ApiApexManagent.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.audience}")
    private String audience;

        private static final String[] SWAGGER_WHITELIST = {
        "/swagger-ui",
        "/swagger-ui/",
        "/swagger-ui.html",
        "/swagger-ui/index.html",
        "/v3/api-docs",
        "/v3/api-docs/",
        "/v3/api-docs.yaml",
        "/swagger-resources",
        "/swagger-resources/",
        "/webjars/"
    };

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        for (String pattern : SWAGGER_WHITELIST) {
            if (path.startsWith(pattern)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String token = header.substring(7);
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)))
                    .requireIssuer(issuer)
                    .requireAudience(audience)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            // Extraer el nombre de usuario del claim principal ("unique_name" o "sub" o "name")
            String username = claims.get("unique_name", String.class);
            if (username == null) {
                username = claims.getSubject();
            }
            if (username == null && claims.get("name") != null) {
                username = claims.get("name", String.class);
            }
            // Extraer el rol del token (por nombre o por id)
            String role = claims.get("role", String.class);
            if (role == null && claims.get("rolid") != null) {
                int rolId = Integer.parseInt(claims.get("rolid").toString());
                switch (rolId) {
                    case 1: role = "Administrador"; break;
                    case 2: role = "Tecnico"; break;
                    case 3: role = "Empleado"; break;
                    default: role = "usuario";
                }
            }
        String authority = "ROLE_" + role;
        System.out.println("[JWT-DEBUG] Username extraído: " + username);
        System.out.println("[JWT-DEBUG] Rol extraído: " + role);
        System.out.println("[JWT-DEBUG] Autoridad construida: " + authority);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
            username, null, java.util.Collections.singletonList(new SimpleGrantedAuthority(authority)));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.setAttribute("claims", claims);
        System.out.println("[JWT] Usuario: " + username + " - Rol: " + role + " - Autoridades: " + authentication.getAuthorities());
        } catch (Exception e) {
            System.err.println("[JWT ERROR] " + e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("JWT ERROR: " + e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }
}
