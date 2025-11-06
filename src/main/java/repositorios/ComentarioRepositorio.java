package repositorios;

import daos.ComentarioDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ComentarioRepositorio extends JpaRepository<ComentarioDAO, Integer> {
    
    // Encontrar todos los comentarios de una tarea
    List<ComentarioDAO> findByTareaId(Integer tareaId);
}
