package config; // O donde pongas tus clases de configuración

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils; // Importante
import org.springframework.web.filter.OncePerRequestFilter;
import servicios.UsuariosServicio; // Tu UserDetailsService

import java.io.IOException;

@Component // <-- ¡Importante! Para que Spring lo detecte y podamos inyectarlo
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider; // Tu generador de tokens

    @Autowired
    private UsuariosServicio usuariosServicio; // Tu servicio de usuarios

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Obtener el token del header "Authorization"
            String jwt = getJwtFromRequest(request);

            // 2. Validar el token
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                
                // 3. Obtener el email (username) del token
                String email = tokenProvider.getUsernameFromJWT(jwt); // O el método que uses
                
                // 4. Cargar el usuario (UserDetails) desde la BBDD
                UserDetails userDetails = usuariosServicio.loadUserByUsername(email);
                
                // 5. Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. ¡LA MAGIA! Establecer al usuario como "autenticado" en el contexto
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            // Error al procesar el token, no hacer nada y dejar que Spring Security
            // lo maneje (seguirá siendo "anónimo" y será bloqueado)
            logger.error("No se pudo establecer la autenticación del usuario", ex);
        }

        filterChain.doFilter(request, response); // Continuar con el resto de filtros
    }

    /**
     * Método auxiliar para extraer el "Bearer <token>" del header
     */
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}