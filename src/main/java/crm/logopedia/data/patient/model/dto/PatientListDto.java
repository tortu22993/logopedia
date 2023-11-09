package crm.logopedia.data.patient.model.dto;

import crm.logopedia.data.services.model.dto.ServicesListDto;
import crm.logopedia.util.abstraction.AbstractListDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientListDto extends AbstractListDto {

    /**
     * Indica si la sección de filtros está abierta. Si su valor es
     * verdadero, entonces en el listado de activos aparecerá la
     * sección de filtros desplegada.
     */
    private boolean openedFiltersSection;

    /**
     * El ID del paciente.
     */
    private Long id;

    private String name;

    /**
     * El nombre completo del paciente.
     */
    private String fullName;

    /**
     * El nombre del colegio del paciente
     */
    private String school;

    /**
     * La edad del paciente
     */
    private Integer age;

    /**
     * Si el paciente se encuentra en activo o no
     */
    private Boolean enabled;

    @Override
    public ServicesListDto convertBlankToNull() {
        return (ServicesListDto) super.convertBlankToNull();
    }
}
