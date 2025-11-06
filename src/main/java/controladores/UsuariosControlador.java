package controladores;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Importamos el DTO de Registro que creamos
import dtos.RegistroUsuarioDTO; 
import dtos.UsuarioDTO;
import servicios.UsuariosServicio;

@RestController
public class UsuariosControlador {

    @Autowired
    private UsuariosServicio usuariosServicio;

    /**
     * GET /usuarios
     * Obtiene una lista de todos los usuarios (sin IDs ni contraseñas).
     * (Este endpoint lo creamos antes)
     */
    @GetMapping("/usuarios")
    public Map<String, Object> getUsuariosData() {
        List<UsuarioDTO> usuarios = usuariosServicio.obtenerTodosLosUsuarios();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("listaDeUsuarios", usuarios); 
        return respuesta;
    }

    /**
     * POST /usuarios
     * Registra un nuevo usuario en la base de datos.
     * (Este endpoint lo creamos antes)
     */
    @PostMapping("/usuarios")
    public ResponseEntity<?> registrarNuevoUsuario(@RequestBody RegistroUsuarioDTO datosRegistro) {
        try {
            // Llama al servicio para registrar
            UsuarioDTO nuevoUsuario = usuariosServicio.registrarNuevoUsuario(datosRegistro);
            
            // Si tiene éxito, devuelve un 201 Created
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
            
        } catch (IllegalStateException e) {
            // Si el servicio lanza la excepción (email duplicado), devuelve un 400 Bad Request
            return new ResponseEntity<>(Map.of("error", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            return new ResponseEntity<>(Map.of("error", "Error interno al registrar el usuario."), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}