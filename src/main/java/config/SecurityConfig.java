package config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.cors.CorsConfigurationSource; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.cors.UrlBasedCorsConfigurationSource; // <-- NUEVA IMPORTACIÓN
import servicios.UsuariosServicio;

import java.util.List; // <-- NUEVA IMPORTACIÓN

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UsuariosServicio servicioUsuarios; 
    
    @Autowired
    private PasswordEncoder codificadorContrasena;

    @Bean
    public AuthenticationManager gestorAutenticacion(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(servicioUsuarios)
                .passwordEncoder(codificadorContrasena)
                .and()
                .build();
    }

    /**
     * Define la "Cadena de Filtros" de seguridad.
     */
    @Bean
    public SecurityFilterChain cadenaFiltrosSeguridad(HttpSecurity http) throws Exception {
        
        http
            // --- ¡NUEVO! Habilitamos CORS usando la configuración de abajo ---
            .cors(cors -> cors.configurationSource(fuenteConfiguracionCors()))

            // 1. Deshabilitar CSRF
            .csrf(csrf -> csrf.disable())

            // 2. Política de Sesión SIN ESTADO
            .sessionManagement(session -> 
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // 3. Reglas de Autorización de Peticiones
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()     
                .requestMatchers("/usuarios").permitAll() 
                .anyRequest().authenticated() 
            );

        return http.build();
    }

    // --- ¡BEAN NUEVO PARA CONFIGURAR CORS! ---
    /**
     * Define la configuración de CORS para toda la aplicación.
     * Esto le dice al navegador que confíe en nuestro front-end.
     */
    @Bean
    CorsConfigurationSource fuenteConfiguracionCors() {
        CorsConfiguration configuracion = new CorsConfiguration();
        
        // Orígenes permitidos (para desarrollo, "*" permite todo)
        configuracion.setAllowedOrigins(List.of("*")); 
        
        // Métodos HTTP permitidos (GET, POST, etc.)
        configuracion.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD"));
        
        // Cabeceras permitidas (importante para tokens)
        configuracion.setAllowedHeaders(List.of("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuracion); // Aplicar a todas las rutas
        
        return source;
    }
}