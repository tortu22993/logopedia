package crm.logopedia.data.contact.repository;

import crm.logopedia.data.contact.model.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    /**
     * Obtiene un contacto según su ID.
     *
     * @param id El ID del contacto a obtener
     * @return El contacto con dicho ID
     */
    @Override
    @EntityGraph(attributePaths = {
            "createdBy.profile",
            "lastModifiedBy.profile"
    })
    @NonNull
    Optional<Contact> findById(@NonNull Long id);

    /**
     * Obtiene un contacto según su correo electrónico.
     *
     * @param email El correo electrónico del contacto a obtener
     * @return El contacto con dicho correo electrónico
     */
    @NonNull Optional<Contact> findByEmail(@NonNull String email);

    /**
     * Obtiene una lista paginada de contactos según el ID del paciente
     * al que están asociados.
     *
     * @param patientId El ID del paciente al que pertenece el contacto
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista paginada con los contactos relacionados al paciente con dicho ID
     */
    @Query("SELECT co FROM Patient AS cu INNER JOIN cu.contacts AS co WHERE cu.id = :patientId")
    @NonNull
    Page<Contact> findByPatientId(@NonNull @Param("patientId") Long patientId, @NonNull Pageable pageable);

    /**
     * Busca una serie de contactos en función de un texto introducido por
     * el usuario. Dicho texto debe estar contenido en el nombre completo
     * del contacto.
     *
     * @param text El texto que se utiliza como filtro
     * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
     * @return Una lista de contactos cuyo nombre coincide con el filtro
     */
    @Query("""
		FROM Contact WHERE
		(
			CASE WHEN lastName IS NOT NULL AND LENGTH(lastName) > 0 THEN
				CONCAT(name, ' ', middleName, ' ', lastName)
			ELSE
				CONCAT(name, ' ', middleName)
			END
		) LIKE %:text%
	""")
    @NonNull
    List<Contact> search(@NonNull @Param("text") String text, @NonNull Pageable pageable);
}
