package servicios;

import daos.UsuarioDAO;
import dtos.RegistroUsuarioDTO;
import dtos.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User; // <-- NUEVA IMPORTACIÓN
import org.springframework.security.core.userdetails.UserDetails; // <-- NUEVA IMPORTACIÓN
import org.springframework.security.core.userdetails.UserDetailsService; // <-- NUEVA IMPORTACIÓN
import org.springframework.security.core.userdetails.UsernameNotFoundException; // <-- NUEVA IMPORTACIÓN
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositorios.UsuarioRepositorio;

import java.time.LocalDateTime;
import java.util.ArrayList; // <-- NUEVA IMPORTACIÓN
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para la entidad Usuario.
 * Se encarga de la lógica (DTOs) Y de la seguridad (UserDetails).
 */
@Service
public class UsuariosServicio implements UserDetailsService { // <-- ¡IMPLEMENTAMOS LA INTERFAZ!

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- ¡NUEVO MÉTODO DE UserDetailsService! ---
    /**
     * Este método es llamado automáticamente por Spring Security cuando
     * intentamos autenticar a un usuario (ej: en el login).
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Buscar el usuario en nuestro repositorio
        UsuarioDAO usuarioDAO = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("No se encontró un usuario con el email: " + email)
                );

        // 2. Convertir nuestro UsuarioDAO en un UserDetails de Spring Security
        return new User(
            usuarioDAO.getEmail(),           // El "username"
            usuarioDAO.getPassword(),        // La contraseña YA HASHEADA de la BD
            new ArrayList<>()               // La lista de roles/autoridades (por ahora vacía)
        );
    }

    // --- LÓGICA DE NEGOCIO (la que ya tenías) ---

    /**
     * Obtiene todos los usuarios y los convierte a DTO.
     */
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepositorio.findAll()
                .stream()
                .map(this::convertirUsuarioA_DTO)
                .collect(Collectors.toList());
    }

    /**
     * Registra un nuevo usuario en el sistema.
     */
    public UsuarioDTO registrarNuevoUsuario(RegistroUsuarioDTO datosRegistro) throws IllegalStateException {
        
        if (usuarioRepositorio.findByEmail(datosRegistro.getEmail()).isPresent()) {
            throw new IllegalStateException("El email " + datosRegistro.getEmail() + " ya está registrado.");
        }

        UsuarioDAO nuevoUsuario = new UsuarioDAO();
        nuevoUsuario.setNombre(datosRegistro.getNombre());
        nuevoUsuario.setEmail(datosRegistro.getEmail());
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        nuevoUsuario.setRolId(2); // Rol de "Usuario General"

        String passwordHasheada = passwordEncoder.encode(datosRegistro.getPassword());
        nuevoUsuario.setPassword(passwordHasheada);
        
        UsuarioDAO usuarioGuardado = usuarioRepositorio.save(nuevoUsuario);

        return convertirUsuarioA_DTO(usuarioGuardado);
    }


    // --- Métodos Privados de Mapeo (Conversión) ---

    private UsuarioDTO convertirUsuarioA_DTO(UsuarioDAO entidad) {
        UsuarioDTO dto = new UsuarioDTO();
        
        dto.setNombre(entidad.getNombre());
        dto.setEmail(entidad.getEmail());
        dto.setAvatarUrl(entidad.getAvatarUrl());
        dto.setActivo(entidad.isActivo());
        dto.setFechaCreacion(entidad.getFechaCreacion());
        
        return dto;
    }
}