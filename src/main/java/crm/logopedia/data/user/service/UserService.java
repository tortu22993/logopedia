package crm.logopedia.data.user.service;

import crm.logopedia.data.user.model.dto.UserDetailDto;
import crm.logopedia.data.user.model.dto.UserListDto;
import crm.logopedia.data.user.model.entity.User;
import jakarta.mail.MessagingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/** Intento de commit
 * Declaración de las funcionalidades relativas a
 * la entidad {@link User} que conectan la capa
 * de peticiones HTTP con las consultas a la base
 * de datos.
 *
 * @author Enrique Escalante
 */
public interface UserService {

	/**
	 * Obtiene el DTO de un usuario según su nombre de usuario.
	 *
	 * @param username El nombre de usuario del usuario a obtener
	 * @return El DTO del usuario con dicho nombre de usuario
	 */
	UserDetailDto findByUsername(String username);

	/**
	 * Obtiene el DTO usuario según su nombre de usuario y, además, está habilitado y
	 * no tiene la cuenta verificada.
	 *
	 * @param username El nombre de usuario del usuario a obtener
	 * @return El DTO del usuario con que cumple las condiciones descritas
	 */
	UserDetailDto findNotVerifiedByUsername(String username);

	/**
	 * Devuelve una lista paginada de usuarios en función de un filtro.
	 *
	 * @param userListDto La plantilla que se utiliza como filtro
	 * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
	 * @return Una lista paginada de DTO de usuarios filtrados
	 */
	Page<UserListDto> findByFilter(UserListDto userListDto, Pageable pageable);

	/**
	 * Busca una serie de usuarios en función de un texto introducido por
	 * el usuario.
	 *
	 * @param text El texto que se utiliza como filtro
	 * @return Una lista de DTO de usuarios cuyo nombre coincide con el filtro
	 */
	List<UserListDto> search(String text);

	/**
	 * Guarda un usuario en función de los datos rellenados en un DTO.
	 *
	 * @param userDetailDto El DTO del usuario a partir del cual se guardará la entidad
	 * @return El DTO del usuario guardado
	 */
	UserDetailDto save(UserDetailDto userDetailDto) throws MessagingException;

	/**
	 * Finaliza la configuración de la cuenta de un usuario tras haber sido dado de alta
	 * y haber verificado su identidad a través de un token temporal.
	 *
	 * @param email El correo electrónico del usuario
	 * @param password La contraseña a establecer
	 */
	void finishAccountConfiguration(String email, String password);


	/**
	 * Obtiene una lista paginada de DTO de usuarios según el ID de la cola de trabajo
	 * al que están asociados.
	 *
	 * @param workQueueId El ID del usuario al que pertenece la cola de trabajo
	 * @param pageable El objeto paginador que indica la página a obtener y el número de resultados
	 * @return Una lista paginada de DTO con los usuarios relacionados la cola de trabajo con dicho ID
	 */
	//Page<UserListDto> findByUsernameWorkQueue(Long workQueueId, Pageable pageable);

	/**
	 * Obtiene la lista completa de usuarios.
	 *
	 * @return La lista completa de usuarios
	 */
	List<UserListDto> findAll();

}
