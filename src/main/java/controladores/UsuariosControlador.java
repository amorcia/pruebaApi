package controladores;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus; // <-- NUEVA IMPORTACIÓN
import org.springframework.http.ResponseEntity; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.bind.annotation.RequestBody; // <-- NUEVA IMPORTACIÓN
import org.springframework.web.bind.annotation.RestController;

import dtos.RegistroUsuarioDTO; // <-- NUEVA IMPORTACIÓN
import dtos.UsuarioDTO;
import servicios.UsuariosServicio;

@RestController
public class UsuariosControlador {

    @Autowired
    private UsuariosServicio usuariosServicio;

    /**
     * GET /usuarios
     * Obtiene una lista de todos los usuarios.
     */
    @GetMapping("/usuarios")
    public Map<String, Object> getUsuariosData() {
        List<UsuarioDTO> usuarios = usuariosServicio.obtenerTodosLosUsuarios();
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("listaDeUsuarios", usuarios); 
        return respuesta;
    }

    // --- ¡NUEVO ENDPOINT DE REGISTRO! ---
    /**
     * POST /usuarios
     * Registra un nuevo usuario en la base de datos.
     * @param datosRegistro JSON con nombre, email y password
     * @return 201 Created con el usuario creado (DTO) o 400 Bad Request si el email ya existe.
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
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Para cualquier otro error inesperado
            return new ResponseEntity<>("Error interno al registrar el usuario.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}