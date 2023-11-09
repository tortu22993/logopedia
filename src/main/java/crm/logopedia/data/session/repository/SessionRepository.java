package crm.logopedia.data.session.repository;

import crm.logopedia.data.session.model.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    /**
     * Obtiene una session según su ID.
     *
     * @param id El ID del session a obtener
     * @return El session con dicho ID
     */
    @Override
    @EntityGraph(attributePaths = {
            "createdBy.profile",
            "lastModifiedBy.profile"
    })
    @NonNull
    Optional<Session> findById(@NonNull Long id);

    /**
     * Obtiene una lista paginada de sessiones según el ID del paciente
     * al que están asociados.
     *
     * @param patientId El ID del paciente al que pertenece la session
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista paginada con las sessiones relacionados al paciente con dicho ID
     */
    @Query("SELECT co FROM Patient AS cu INNER JOIN cu.sessions AS co WHERE cu.id = :patientId")
    @NonNull
    Page<Session> findByPatientId(@NonNull @Param("patientId") Long patientId, @NonNull Pageable pageable);
}
