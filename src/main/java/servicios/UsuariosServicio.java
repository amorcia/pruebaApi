package servicios;

import dtos.RegistroUsuarioDTO;
import dtos.UsuarioDTO;
import entidades.RolDAO;
import entidades.UsuarioDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority; // <-- ¡NUEVO IMPORT!
import org.springframework.security.core.authority.SimpleGrantedAuthority; // <-- ¡NUEVO IMPORT!
import org.springframework.security.core.context.SecurityContextHolder; // <-- ¡NUEVO IMPORT!
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import repositorios.RolRepositorio; 
import repositorios.UsuarioRepositorio;
import org.springframework.transaction.annotation.Transactional; 

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional; // <-- ¡NUEVO IMPORT!

@Service
public class UsuariosServicio implements UserDetailsService { 

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private RolRepositorio rolRepositorio;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Lógica de Seguridad (Implementación de UserDetailsService) ---
    /**
     * (MODIFICADO)
     * Carga el usuario Y sus roles para Spring Security.
     */
    @Override
    @Transactional(readOnly = true) // Buena práctica para métodos de solo lectura
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        
        // 1. Buscar el usuario
        UsuarioDAO usuarioDAO = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> 
                    new UsernameNotFoundException("No se encontró un usuario con el email: " + email)
                );

        // 2. Buscar el Rol (para obtener el ID)
        // Usamos el ID de Rol (1, 2, 7, etc.) como la "Autoridad"
        String rolId = usuarioDAO.getRolId().toString();
        
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rolId)); // Ej: "ROLE_1", "ROLE_2"

        // 3. Convertir a UserDetails de Spring Security
        return new User(
            usuarioDAO.getEmail(),          // El "username"
            usuarioDAO.getPassword(),       // La contraseña YA HASHEADA de la BD
            authorities                     // ¡La lista de roles!
        );
    }

    // --- LÓGICA DE NEGOCIO (CRUD) ---

    /**
     * Obtiene todos los usuarios y los convierte a DTO.
     */
    @Transactional(readOnly = true)
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepositorio.findAll()
                .stream()
                .map(this::convertirUsuarioA_DTO)
                .collect(Collectors.toList());
    }

    /**
     * (MODIFICADO)
     * Registra un nuevo usuario con contraseña 'tsk006' por defecto.
     */
    @Transactional
    public UsuarioDTO registrarNuevoUsuario(RegistroUsuarioDTO datosRegistro) throws IllegalStateException {
        
        if (usuarioRepositorio.findByEmail(datosRegistro.getEmail()).isPresent()) {
            throw new IllegalStateException("El email " + datosRegistro.getEmail() + " ya está registrado.");
        }

        UsuarioDAO nuevoUsuario = new UsuarioDAO();
        nuevoUsuario.setNombre(datosRegistro.getNombre());
        nuevoUsuario.setEmail(datosRegistro.getEmail());
        nuevoUsuario.setActivo(datosRegistro.getActivo());
        nuevoUsuario.setFechaCreacion(LocalDateTime.now());
        
        // --- LÓGICA DE ROL (CORREGIDA) ---
        // Asignar el rolId que VIENE del DTO (del formulario)
        nuevoUsuario.setRolId(datosRegistro.getRolId()); 
        // --- FIN LÓGICA DE ROL ---
        
        // --- ¡LÓGICA DE CONTRASEÑA POR DEFECTO! ---
        // Asignamos 'tsk006' hasheada
        nuevoUsuario.setPassword(passwordEncoder.encode("tsk006"));
        
        // Dejamos 'fechaUltimaSesion' como NULL para forzar el cambio
        nuevoUsuario.setFechaUltimaSesion(null);
        // --- FIN LÓGICA DE CONTRASEÑA ---
        
        UsuarioDAO usuarioGuardado = usuarioRepositorio.save(nuevoUsuario);

        return convertirUsuarioA_DTO(usuarioGuardado);
    }

    /**
     * (MODIFICADO)
     * Actualiza un usuario (SIN TOCAR LA CONTRASEÑA).
     */
    @Transactional
    public UsuarioDTO actualizarUsuarioPorEmail(String emailOriginal, RegistroUsuarioDTO datosNuevos) throws UsernameNotFoundException, IllegalStateException {
        
        UsuarioDAO usuario = usuarioRepositorio.findByEmail(emailOriginal)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con email: " + emailOriginal));

        if (!emailOriginal.equals(datosNuevos.getEmail()) && usuarioRepositorio.findByEmail(datosNuevos.getEmail()).isPresent()) {
            throw new IllegalStateException("El nuevo email " + datosNuevos.getEmail() + " ya está en uso.");
        }

        // 3. Actualizar los campos
        usuario.setNombre(datosNuevos.getNombre());
        usuario.setEmail(datosNuevos.getEmail());
        usuario.setActivo(datosNuevos.getActivo());

        // 4. --- LÓGICA DE ROL (CORREGIDA) ---
        // Asignamos el rolId que VIENE del DTO
        usuario.setRolId(datosNuevos.getRolId());
        // --- FIN LÓGICA DE ROL ---

        // 5. NO tocamos la contraseña

        // 6. Guardar y devolver DTO
        UsuarioDAO usuarioActualizado = usuarioRepositorio.save(usuario);
        return convertirUsuarioA_DTO(usuarioActualizado);
    }

    /**
     * (MODIFICADO)
     * Elimina un usuario por su email CON LÓGICA DE SEGURIDAD.
     */
    @Transactional
    public void eliminarUsuarioPorEmail(String email) throws UsernameNotFoundException, IllegalStateException {
        
        // 1. Obtener quién está borrando (Admin/Owner logueado)
        String adminEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        UsuarioDAO admin = usuarioRepositorio.findByEmail(adminEmail)
            .orElseThrow(() -> new IllegalStateException("No se pudo encontrar al administrador logueado."));

        // 2. Obtener a quién se va a borrar
        UsuarioDAO usuario = usuarioRepositorio.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró usuario con email: " + email));
        
        // --- 3. REGLAS DE SEGURIDAD ---
        if (admin.getEmail().equals(usuario.getEmail())) {
            throw new IllegalStateException("No puedes borrarte a ti mismo.");
        }
        if (admin.getRolId() == 2 && (usuario.getRolId() == 1 || usuario.getRolId() == 2)) {
            throw new IllegalStateException("Un Administrador no puede borrar a un Owner u otro Administrador.");
        }
        // (Owner (1) sí puede borrar a un Admin (2))
        
        // 4. Si pasa las reglas, eliminar
        usuarioRepositorio.delete(usuario);
    }
    
    // --- ¡NUEVO MÉTODO AÑADIDO! ---
    /**
     * Elimina una lista de usuarios (para el borrado múltiple).
     */
    @Transactional
    public void eliminarUsuariosPorEmail(List<String> emails) throws UsernameNotFoundException, IllegalStateException {
        // Simplemente llamamos al método individual, que ya tiene la lógica de seguridad
        for (String email : emails) {
            this.eliminarUsuarioPorEmail(email);
        }
    }


    // --- Método de Mapeo (Actualizado) ---

    /**
     * (MODIFICADO)
     * Convierte DAO a DTO. AÑADIMOS ROLID para el frontend.
     */
    private UsuarioDTO convertirUsuarioA_DTO(UsuarioDAO entidad) {
        UsuarioDTO dto = new UsuarioDTO();
        
        dto.setNombre(entidad.getNombre());
        dto.setEmail(entidad.getEmail());
        dto.setAvatarUrl(entidad.getAvatarUrl());
        dto.setActivo(entidad.isActivo());
        dto.setFechaCreacion(entidad.getFechaCreacion());
        dto.setFechaUltimaSesion(entidad.getFechaUltimaSesion());

        // --- LÓGICA DE ROL (Traducir ID a Nombre) ---
        String rolNombre = rolRepositorio.findById(entidad.getRolId())
                                 .map(RolDAO::getNombre)
                                 .orElse("N/A"); 
        dto.setRolNombre(rolNombre);
        
        // --- ¡ROL ID AÑADIDO! ---
        // Es necesario para el modal de EDICIÓN (para saber qué <option> seleccionar)
        // y para la seguridad de borrado en el frontend.
        dto.setRolId(entidad.getRolId());
        // --- FIN LÓGICA DE ROL ---
        
        return dto;
    }
}