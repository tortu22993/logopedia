package crm.logopedia.data.jobposition.repository;

import crm.logopedia.data.jobposition.model.entity.JobPosition;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Repositorio de la clase {@link JobPosition} que extiende de {@link JpaRepository},
 * una interfaz que permite implementar métodos de consulta y ejecución contra una base de datos.
 * También permite obtener listados paginados.
 *
 * @author Enrique Escalante
 */
public interface JobPositionRepository extends JpaRepository<JobPosition, Long> {

	/**
	 * Obtiene un equipo según su ID.
	 *
	 * @param id El ID del equipo a obtener
	 * @return El puesto de trabajo con dicho ID
	 */
	@Override
	@EntityGraph(attributePaths = {
		"createdBy.profile",
		"lastModifiedBy.profile"
	})
	@NonNull Optional<JobPosition> findById(@NonNull Long id);

}
