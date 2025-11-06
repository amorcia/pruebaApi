package controladores;

import dtos.ProyectoDTO;
import dtos.UsuarioDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import servicios.DashboardServicio; // <-- ¡CORREGIDO! Ahora importa el servicio correcto.

import java.util.List;

/**
 * Controlador MVC para la vista "Dashboard".
 * Sigue el modelo del profesor:
 * 1. Un Controlador por Vista.
 * 2. Inyecta UN servicio de lógica (`DashboardService`).
 */
@Controller
public class DashboardControlador {

    // --- CORRECCIÓN ---
    // El controlador ahora solo conoce UN servicio, el de su lógica.
    @Autowired
    private DashboardServicio dashboardService;

    /**
     * Gestiona la petición a la página principal del dashboard.
     * URL: /taskflow/v1/dashboard
     * (Asumiendo que tu context-path es /taskflow/v1)
     */
    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {

        // --- CORRECCIÓN ---
        // 1. Pedimos los datos al ÚNICO servicio
        List<UsuarioDTO> usuarios = dashboardService.obtenerTodosLosUsuarios();
        List<ProyectoDTO> proyectos = dashboardService.obtenerTodosLosProyectos();

        // 2. Añadimos los datos al "Model" para el HTML
        model.addAttribute("listaDeUsuarios", usuarios);
        model.addAttribute("listaDeProyectos", proyectos);

        // 3. Devolvemos el nombre del HTML
        return "dashboard"; // Busca en /resources/templates/dashboard.html
    }
}

