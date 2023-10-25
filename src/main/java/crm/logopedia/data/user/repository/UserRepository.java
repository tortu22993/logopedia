package crm.logopedia.data.user.repository;

import crm.logopedia.data.user.model.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de la clase {@link User} que extiende de {@link JpaRepository},
 * una interfaz que permite implementar métodos de consulta y ejecución contra una base de datos.
 * También permite obtener listados paginados.
 * 
 * @author Enrique Escalante
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
	 * Obtiene un usuario según su nombre de usuario.
	 * 
	 * @param username El nombre de usuario del usuario a obtener
	 * @return El usuario con dicho nombre de usuario
	 */
    @NonNull Optional<User> findByUsername(@NonNull String username);
    
    /**
	 * Obtiene un usuario según su correo electrónico.
	 * 
	 * @param email El correo electrónico del usuario a obtener
	 * @return El usuario con dicho correo electrónico
	 */
    @NonNull Optional<User> findByEmail(@NonNull String email);

	/**
	 * Obtiene un usuario junto con los datos de su perfil
	 * según su correo electrónico.
	 * 
	 * @param email El correo electrónico del usuario a obtener
	 * @return El usuario con dicho correo electrónico
	 */
	@EntityGraph(attributePaths = { "profile" })
	@Query("FROM User AS u WHERE u.email LIKE :email")
    @NonNull Optional<User> findByEmailWithProfile(@NonNull @Param("email") String email);

	/**
	 * Obtiene un usuario según su nombre de usuario y, además, no está habilitado y no tiene la cuenta
	 * verificada.
	 *
	 * @param username El nombre de usuario del usuario a obtener
	 * @return El usuario con que cumple las condiciones descritas
	 */
	@Query("SELECT u FROM User AS u WHERE u.enabled = false AND u.verifiedAccount = false AND u.username LIKE :username")
	@NonNull Optional<User> findNotVerifiedByUsername(@NonNull @Param("username") String username);

	@Override
	@EntityGraph(attributePaths = { "profile" })
	@NonNull <S extends User> Page<S> findAll(@NonNull Example<S> example, @NonNull Pageable pageable);

	/**
	 * Busca una serie de usuarios en función de un texto introducido por
	 * el usuario. Dicho texto debe estar contenido en el nombre completo
	 * del usuario.
	 *
	 * @param text El texto que se utiliza como filtro
	 * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
	 * @return Una lista de usuarios cuyo nombre coincide con el filtro
	 */
	@Query("""
		FROM User
		INNER JOIN profile AS p WHERE
		(
			CASE WHEN p.lastName IS NOT NULL AND LENGTH(p.lastName) > 0 THEN
				CONCAT(p.name, ' ', p.middleName, ' ', p.lastName)
			ELSE
				CONCAT(p.name, ' ', p.middleName)
			END
		) LIKE %:text%
	""")
	@NonNull List<User> search(@NonNull @Param("text") String text, @NonNull Pageable pageable);

	/**
	 * Actualiza el número de intentos que el usuario realiza para
	 * iniciar sesión.
	 * 
	 * @param loginAttempts El número de intentos a establecer
	 * @param email El correo electrónico del usuario a actualizar
	 */
	@Query("UPDATE User AS u SET u.loginAttempts = :login_attempts WHERE u.email LIKE :email")
	@Modifying
	void updateLoginAttempts(@NonNull @Param("login_attempts") Integer loginAttempts, @NonNull @Param("email") String email);

	/**
	 * Finaliza la configuración de la cuenta de un nuevo usuario. Para ello, se establece su estado a <u>habilitado</u>,
	 * se marca su <u>cuenta como verificada</u> y se actualiza el valor de su <u>contraseña</u> a partir del valor que él mismo
	 * ha introducido.
	 *
	 * @param email El correo electrónico del usuario a actualizar
	 * @param password La nueva contraseña encriptada a establecer al usuario
	 */
	@Query("UPDATE User AS u SET u.enabled = true, u.verifiedAccount = true, u.password = :password WHERE u.email LIKE :email")
	@Modifying
	void finishAccountConfiguration(@NonNull @Param("email") String email, @NonNull @Param("password") String password);

	/**
	 * Obtiene una lista paginada de usuarios según el ID de la cola de trabajo
	 * al que están asociados.
	 *
	 * @param workQueueId El ID del usuario al que pertenece la cola de trabajo
	 * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
	 * @return Una lista paginada con los usuarios relacionados a la cola de trabajo con dicho ID
	 */
	/*@Query("SELECT U FROM WorkQueue AS W INNER JOIN W.users AS U WHERE W.id = :workQueueId")
	Page<User> findByWorkQueueId(@Param("workQueueId") Long workQueueId, Pageable pageable);*/
}
