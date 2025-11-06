package servicios;

import daos.ProyectoDAO; // Importamos las Entidades (DAOs)
import daos.UsuarioDAO;
import dtos.ProyectoDTO; // Importamos los DTOs (para devolver datos limpios)
import dtos.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositorios.ProyectoRepositorio; // Importamos los Repositorios
import repositorios.UsuarioRepositorio;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Este es un Servicio de "Caso de Uso" o "Lógica de Negocio".
 * No representa a UNA entidad, sino a la LÓGICA de una característica
 * (en este caso, el Dashboard).
 * Por eso, PUEDE USAR MÚLTIPLES REPOSITORIOS.
 */
@Service
public class DashboardServicio {

    // El servicio SÍ puede inyectar todos los repositorios que necesite
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ProyectoRepositorio proyectoRepositorio;

    // --- Lógica Principal ---

    /**
     * Obtiene todos los usuarios y los convierte a DTO.
     */
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        return usuarioRepositorio.findAll() // 1. Obtiene Entidades (DAO)
                .stream()
                .map(this::convertirUsuarioA_DTO) // 2. Convierte a DTO
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los proyectos y los convierte a DTO.
     */
    public List<ProyectoDTO> obtenerTodosLosProyectos() {
        return proyectoRepositorio.findAll()
                .stream()
                .map(this::convertirProyectoA_DTO)
                .collect(Collectors.toList());
    }


    // --- Métodos Privados de Mapeo (Conversión) ---
    // El servicio se encarga de convertir Entidades (DAOs) a DTOs

    private UsuarioDTO convertirUsuarioA_DTO(UsuarioDAO entidad) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setEmail(entidad.getEmail());
        dto.setAvatarUrl(entidad.getAvatarUrl());
        dto.setRolId(entidad.getRolId());
        // Fíjate que no pasamos la contraseña ni datos sensibles
        return dto;
    }

    private ProyectoDTO convertirProyectoA_DTO(ProyectoDAO entidad) {
        ProyectoDTO dto = new ProyectoDTO();
        dto.setId(entidad.getId());
        dto.setNombre(entidad.getNombre());
        dto.setDescripcion(entidad.getDescripcion());
        dto.setTipo(entidad.getTipo());
        dto.setColor(entidad.getColor());
        dto.setCreadorId(entidad.getCreadorId());
        dto.setEquipoId(entidad.getEquipoId());
        dto.setFechaInicio(entidad.getFechaInicio());
        dto.setFechaFin(entidad.getFechaFin());
        dto.setEstado(entidad.getEstado());
        dto.setProgreso(entidad.getProgreso());
        return dto;
    }
}
