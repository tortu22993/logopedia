package crm.logopedia.data.role.repository;

import crm.logopedia.data.role.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de la clase {@link Role} que extiende de {@link JpaRepository},
 * una interfaz que permite implementar métodos de consulta y ejecución contra una base de datos.
 * También permite obtener listados paginados.
 *
 * @author Enrique Escalante
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

}
