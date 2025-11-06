package controladores;

import daos.RolDAO;
import daos.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import repositorios.RolRepositorio;
import repositorios.UsuarioRepositorio;
import java.time.LocalDateTime;

@Component
@Transactional
public class Crud implements CommandLineRunner {

    @Autowired
    private RolRepositorio rolRepo;
    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- [PruebaCrudRunner]: Verificando datos iniciales... ---");

        // --- ¡NUEVA LÓGICA DE VERIFICACIÓN! ---
        // Comprobamos si el usuario admin ya existe antes de hacer nada.
        if (usuarioRepo.findByEmail("admin@taskflow.com").isPresent()) {
            System.out.println("El usuario 'admin' ya existe. Omitiendo creación de datos iniciales.");
        } else {
            // Si no existe, ejecutamos la lógica de creación
            crearDatosIniciales();
        }
        
        System.out.println("--- [PruebaCrudRunner]: Finalizado. El servidor web sigue corriendo. ---");
    }

    /**
     * Método privado para encapsular la lógica de creación
     */
    private void crearDatosIniciales() {
        System.out.println("Creando nuevos datos iniciales...");
        try {
            // --- 1. CREAR ROL ---
            RolDAO nuevoRol = new RolDAO();
            nuevoRol.setNombre("Admin");
            nuevoRol.setColor("#FF0000");
            rolRepo.save(nuevoRol);
            System.out.println("Rol 'Admin' creado."); // <-- ID Ocultado

            // --- 2. CREAR USUARIO ADMIN ---
            UsuarioDAO nuevoUsuario = new UsuarioDAO();
            nuevoUsuario.setNombre("admin");
            nuevoUsuario.setEmail("admin@taskflow.com");
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setRolId(nuevoRol.getId()); // <-- El ID se usa internamente
            nuevoUsuario.setFechaCreacion(LocalDateTime.now());
            
            // Codificamos la contraseña
            String passwordPlana = "tsk006";
            String passwordHasheada = passwordEncoder.encode(passwordPlana);
            nuevoUsuario.setPassword(passwordHasheada);
            
            usuarioRepo.save(nuevoUsuario);
            System.out.println("Usuario 'admin' creado."); // <-- ID Ocultado
            
            System.out.println("\n--- [PruebaCrudRunner]: DATOS INICIALES CREADOS CON ÉXITO ---");

        } catch (Exception e) {
            System.err.println("\n--- [PruebaCrudRunner]: ERROR AL INSERTAR DATOS INICIALES ---");
            e.printStackTrace();
        }
    }
}