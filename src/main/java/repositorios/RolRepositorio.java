package repositorios;

import daos.RolDAO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepositorio extends JpaRepository<RolDAO, Integer> {
    // Spring Data JPA crea automáticamente los métodos:
    // save(), findById(), findAll(), deleteById(), etc.
}
