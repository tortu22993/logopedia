package crm.logopedia.data.session.model.dto;

import crm.logopedia.util.abstraction.AbstractListDto;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SessionListDto extends AbstractListDto {

    /**
     * Indica si la sección de filtros está abierta. Si su valor es
     * verdadero, entonces en el listado de activos aparecerá la
     * sección de filtros desplegada.
     */
    private boolean openedFiltersSection;

    /**
     * El ID del contacto.
     */
    private Long id;

    /**
     * El id del paciente
     */
    private Long patientId;

    /**
     * El nombre del paciente
     */
    private String patientName;

    /**
     * El id del servicio
     */
    private Long serviceId;
    /**
     * El nombre del servicio
     */
    private String serviceName;

    /**
     * El nombre de usuario del propietario de la session.
     */
    private String ownerUsername;

    /**
     * El nombre completo del propietario de la session.
     */
    private String ownerFullName;

    /**
     * La fecha de la session
     */
    private Date sessionDate;

    /**
     * El asunto de la session
     */
    private String subject;

    @Override
    public SessionListDto convertBlankToNull() {
        return (SessionListDto) super.convertBlankToNull();
    }
}
