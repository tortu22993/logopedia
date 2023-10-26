package crm.logopedia.data.services.repository;

import crm.logopedia.data.services.model.entity.Services;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

public interface ServicesRepository extends JpaRepository<Services, Long> {

    /**
     * Obtiene un servicio según su ID.
     *
     * @param id El ID del servicio a obtener
     * @return El servicio con dicho ID
     */
    @Override
    @EntityGraph(attributePaths = {
            "createdBy.profile",
            "lastModifiedBy.profile"
    })
    @NonNull
    Optional<Services> findById(@NonNull Long id);
}
