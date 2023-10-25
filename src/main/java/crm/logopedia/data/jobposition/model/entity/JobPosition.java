package crm.logopedia.data.jobposition.model.entity;

import crm.logopedia.util.abstraction.AbstractAuditableEntity;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que hace referencia a los puestos de trabajo de la aplicación.
 * Se mapea contra la tabla <strong>PuestosTrabajo</strong> en la base de datos.
 *
 * @author Enrique Escalante
 */
@Entity
@Table(name = "PuestosTrabajo")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPosition extends AbstractAuditableEntity {

	/**
	 * El ID del equipo.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	/**
	 * El nombre del equipo.
	 */
	@Column(name = "nombre", nullable = false, unique = true)
	private String name;



}
