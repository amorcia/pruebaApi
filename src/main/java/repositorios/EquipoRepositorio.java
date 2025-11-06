package repositorios;

import daos.EquipoDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoRepositorio extends JpaRepository<EquipoDAO, Integer> {
    // Métodos automáticos: save(), findById(), findAll(), deleteById()...
}
