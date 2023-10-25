package crm.logopedia.data.jobposition.model.dto;

import crm.logopedia.data.jobposition.model.entity.JobPosition;
import crm.logopedia.util.abstraction.AbstractListDto;
import lombok.*;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * en la vista del listado de la entidad {@link JobPosition}.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPositionListDto extends AbstractListDto {

	/**
	 * Indica si la sección de filtros está abierta. Si su valor es
	 * verdadero, entonces en el listado de activos aparecerá la
	 * sección de filtros desplegada.
	 */
	private boolean openedFiltersSection;

	/**
	 * El ID del puesto de trabajo.
	 */
	private Long id;

	/**
	 * El nombre del puesto de trabajo.
	 */
	private String name;

	@Override
	public JobPositionListDto convertBlankToNull() {
		return (JobPositionListDto) super.convertBlankToNull();
	}

}
