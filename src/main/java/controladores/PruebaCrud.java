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
public class PruebaCrud implements CommandLineRunner {

    @Autowired
    private RolRepositorio rolRepo;
    @Autowired
    private UsuarioRepositorio usuarioRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n--- [PruebaCrudRunner]: Insertando datos iniciales... ---");

        try {
            // --- 1. CREAR ROL ---
            RolDAO nuevoRol = new RolDAO();
            nuevoRol.setNombre("Admin");
            nuevoRol.setColor("#FF0000");
            rolRepo.save(nuevoRol);
            System.out.println("Rol 'Admin' creado con ID: " + nuevoRol.getId());

            // --- 2. CREAR USUARIO ADMIN ---
            UsuarioDAO nuevoUsuario = new UsuarioDAO();
            nuevoUsuario.setNombre("admin");
            nuevoUsuario.setEmail("admin@taskflow.com");
            nuevoUsuario.setActivo(true);
            nuevoUsuario.setRolId(nuevoRol.getId());
            nuevoUsuario.setFechaCreacion(LocalDateTime.now());
            
            // Codificamos la contraseña
            String passwordPlana = "tsk006";
            String passwordHasheada = passwordEncoder.encode(passwordPlana);
            nuevoUsuario.setPassword(passwordHasheada);
            
            usuarioRepo.save(nuevoUsuario);
            System.out.println("Usuario 'admin' creado con ID: " + nuevoUsuario.getId());
            
            System.out.println("\n--- [PruebaCrudRunner]: DATOS INICIALES CREADOS CON ÉXITO ---");

        } catch (Exception e) {
            System.err.println("\n--- [PruebaCrudRunner]: ERROR AL INSERTAR DATOS INICIALES ---");
            e.printStackTrace();
        }

        System.out.println("--- [PruebaCrudRunner]: Finalizado. El servidor web sigue corriendo. ---");
    }
}