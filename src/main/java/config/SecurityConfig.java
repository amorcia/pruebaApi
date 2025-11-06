package config; // Asegúrate de que el paquete sea correcto

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Deshabilitar CSRF (Correcto, lo mantenemos)
            .csrf(csrf -> csrf.disable())

            // 2. Definir las reglas de autorización
            .authorizeHttpRequests(auth -> auth
                
                // --- ¡ESTA ES LA LÍNEA CORREGIDA! ---
                // Le decimos que permita CUALQUIER ruta (/**)
                // que venga después del context-path (/taskflow/v1)
                .requestMatchers("/**").permitAll() 
                
                // Esta línea ya no es estrictamente necesaria
                // .anyRequest().authenticated() 
            );

        return http.build();
    }
}