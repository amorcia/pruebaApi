package repositorios;

import daos.EtiquetaDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EtiquetaRepositorio extends JpaRepository<EtiquetaDAO, Integer> {
    
    List<EtiquetaDAO> findByProyectoId(Integer proyectoId);
}
