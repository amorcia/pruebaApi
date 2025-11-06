package servicios;

import daos.UsuarioDAO;
import dtos.RegistroUsuarioDTO; // <-- NUEVA IMPORTACIÓN
import dtos.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- NUEVA IMPORTACIÓN
import org.springframework.stereotype.Service;
import repositorios.UsuarioRepositorio;

import java.time.LocalDateTime; // <-- NUEVA IMPORTACIÓN
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de lógica de negocio para la entidad Usuario.
 * Se encarga de la lógica y la conversión de DAO a DTO.
 */
@Service
public class UsuariosServicio {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Inyectamos el codificador de contraseñas
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios y los convierte a DTO.
     */
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepositorio.findAll() // 1. Obtiene Entidades (DAO)
                .stream()
                .map(this::convertirUsuarioA_DTO) // 2. Convierte a DTO
                .collect(Collectors.toList());
    }

    // --- ¡NUEVO MÉTODO DE REGISTRO! ---
    /**
     * Registra un nuevo usuario en el sistema.
     * @param datosRegistro DTO con los datos del nuevo usuario.
     * @return El nuevo usuario, convertido a UsuarioDTO.
     * @throws IllegalStateException si el email ya está en uso.
     */
    public UsuarioDTO registrarNuevoUsuario(RegistroUsuarioDTO datosRegistro) throws IllegalStateException {
        
        // 1. VERIFICAMOS SI EL EMAIL YA EXISTE (Lógica de negocio)
        if (usuarioRepositorio.findByEmail(datosRegistro.getEmail()).isPresent()) {
            throw new IllegalStateException("El email " + datosRegistro.getEmail() + " ya está registrado.");
        }

        // 2. Si no existe, creamos el usuario DAO
        UsuarioDAO nuevoUsuario = new UsuarioDAO();
        nuevoUsuario.setNombre(datosRegistro.getNombre());
        nuevoUsuario.setEmail(datosRegistro.getEmail());
        nuevoUsuario.setActivo(true);
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        
        // Asignamos un Rol por defecto (ej: '2' para "Usuario General")
        // (El '1' lo reservamos para el Admin que creamos en el Crud.java)
        nuevoUsuario.setRolId(2); 

        // 3. Hasheamos la contraseña
        String passwordHasheada = passwordEncoder.encode(datosRegistro.getPassword());
        nuevoUsuario.setPassword(passwordHasheada);
        
        // 4. Guardamos en la BD
        UsuarioDAO usuarioGuardado = usuarioRepositorio.save(nuevoUsuario);

        // 5. Devolvemos el DTO (sin contraseña)
        return convertirUsuarioA_DTO(usuarioGuardado);
    }


    // --- Métodos Privados de Mapeo (Conversión) ---

    private UsuarioDTO convertirUsuarioA_DTO(UsuarioDAO entidad) {
        UsuarioDTO dto = new UsuarioDTO();
        
        // Mapeamos los campos del DTO (sin IDs)
        dto.setNombre(entidad.getNombre());
        dto.setEmail(entidad.getEmail());
        dto.setAvatarUrl(entidad.getAvatarUrl());
        dto.setActivo(entidad.isActivo());
        dto.setFechaCreacion(entidad.getFechaCreacion());
        
        return dto;
    }
}