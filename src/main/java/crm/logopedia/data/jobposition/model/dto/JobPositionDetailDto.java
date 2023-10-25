package crm.logopedia.data.jobposition.model.dto;


import crm.logopedia.data.jobposition.model.entity.JobPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * en la vista de detalle de la entidad {@link JobPosition}.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPositionDetailDto {

	/**
	 * El ID del puesto de trabajo.
	 */
	private Long id;

	/**
	 * El nombre del puesto de trabajo.
	 */
	@NotBlank
	@Size(max = 255)
	private String name;

	/**
	 * El nombre de usuario del usuario creador del puesto de trabajo.
	 */
	private String createdByUsername;

	/**
	 * El nombre completo del usuario creador del puesto de trabajo.
	 */
	private String createdByFullName;

	/**
	 * El nombre de usuario del último usuario modificador del puesto de trabajo.
	 */
	private String lastModifiedByUsername;

	/**
	 * El nombre completo del último usuario modificador del puesto de trabajo.
	 */
	private String lastModifiedByFullName;

	/**
	 * La fecha de creación del puesto de trabajo.
	 */
	private Date createdAt;

	/**
	 * La última fecha de modificación del puesto de trabajo.
	 */
	private Date lastModifiedAt;

}
