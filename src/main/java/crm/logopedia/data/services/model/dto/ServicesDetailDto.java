package crm.logopedia.data.services.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicesDetailDto {
    /**
     * El ID del servicio medico.
     */
    private Long id;

    /**
     * El nombre del servicio medico.
     */
    @NotBlank
    @Size(max = 255)
    private String name;

    /**
     * El nombre de usuario del usuario creador del servicio medico.
     */
    private String createdByUsername;

    /**
     * El nombre completo del usuario creador del servicio medico.
     */
    private String createdByFullName;

    /**
     * El nombre de usuario del último usuario modificador del servicio medico.
     */
    private String lastModifiedByUsername;

    /**
     * El nombre completo del último usuario modificador del servicio medico.
     */
    private String lastModifiedByFullName;

    /**
     * La fecha de creación del servicio medico.
     */
    private Date createdAt;

    /**
     * La última fecha de modificación del servicio medico.
     */
    private Date lastModifiedAt;
}
