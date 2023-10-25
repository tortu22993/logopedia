package crm.logopedia.data.role.service;



import crm.logopedia.data.role.model.dto.RoleDto;
import crm.logopedia.data.role.model.entity.Role;

import java.util.List;

/**
 * Declaración de las funcionalidades relativas a
 * la entidad {@link Role} que conectan la capa
 * de peticiones HTTP con las consultas a la base
 * de datos.
 *
 * @author Enrique Escalante
 */
public interface RoleService {

	/**
	 * Obtiene la lista completa de roles.
	 *
	 * @return La lista completa de roles
	 */
	List<RoleDto> findAll();

}
