package crm.logopedia.data.contact.model.dto;

import crm.logopedia.data.contact.model.entity.Contact;
import crm.logopedia.util.common.dto.LocationDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Date;

/**
 * El objeto utilizado para la transferencia de datos (DTO)
 * en la vista de detalle de la entidad {@link Contact}.
 *
 * @author Enrique Escalante
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDetailDto {

    /**
     * El ID del contacto.
     */
    private Long id;

    /**
     * El nombre del contacto.
     */
    @NotBlank
    @Size(max = 30)
    private String name;

    /**
     * El primer apellido del contacto.
     */
    @NotBlank
    @Size(max = 30)
    private String middleName;

    /**
     * El segundo apellido del contacto.
     */
    @Size(max = 30)
    private String lastName;

    /**
     * El nombre completo del contacto.
     */
    private String fullName;

    /**
     * El parentesco del contacto.
     */
    @Size(max = 60)
    private String parentesque;

    /**
     * El teléfono del contacto.
     */
    @Size(max = 15)
    private String telephone;

    /**
     * El teléfono móvil del contacto.
     */
    @Size(max = 15)
    private String mobilePhone;

    /**
     * El correo electrónico del contacto.
     */
    @Email
    private String email;

    /**
     * La localización del contacto.
     */
    @Valid
    @NotNull
    private LocationDto location;

    /**
     * El ID del paciente al que pertenece el contacto.
     */
    @NotNull
    private Long patientId;

    /**
     * El nombre del paciente al que pertenece el contacto.
     */
    private String patientName;

    /**
     * El nombre de usuario del usuario creador del cliente.
     */
    private String createdByUsername;

    /**
     * El nombre completo del usuario creador del cliente.
     */
    private String createdByFullName;

    /**
     * El nombre de usuario del último usuario modificador del cliente.
     */
    private String lastModifiedByUsername;

    /**
     * El nombre completo del último usuario modificador del cliente.
     */
    private String lastModifiedByFullName;

    /**
     * La fecha de creación del cliente.
     */
    private Date createdAt;

    /**
     * La última fecha de modificación del cliente.
     */
    private Date lastModifiedAt;

}
