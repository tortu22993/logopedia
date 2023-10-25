package crm.logopedia.data.services.model.dto;

import crm.logopedia.util.abstraction.AbstractListDto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicesListDto extends AbstractListDto {

    /**
     * Indica si la sección de filtros está abierta. Si su valor es
     * verdadero, entonces en el listado de activos aparecerá la
     * sección de filtros desplegada.
     */
    private boolean openedFiltersSection;

    /**
     * El ID del servicio medico.
     */
    private Long id;

    /**
     * El nombre del servicio medico.
     */
    private String name;

    @Override
    public ServicesListDto convertBlankToNull() {
        return (ServicesListDto) super.convertBlankToNull();
    }
}
