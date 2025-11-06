package config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 1. Una clave secreta MUY segura.
    // (En una app real, esto NUNCA debe estar en el código, iría en application.properties)
    private final Key jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // 2. Tiempo de expiración del token (ej: 1 hora)
    private final long jwtExpirationInMs = 1000 * 60 * 60; // 1 hora

    /**
     * Genera un token JWT para un usuario que se ha logueado con éxito.
     */
    public String generateToken(Authentication authentication) {
        
        String username = authentication.getName(); // El email del usuario
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}